package com.notesdea.articles.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.notesdea.articles.BaseApplication;
import com.notesdea.articles.data.DBManager;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by notes on 2016/12/6.
 */

//网络相关的工具类
public class NetworkUtils {

    //判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    /**
     * 如果缓存里有数据，先显示缓存的数据。再加载服务端数据。
     * @param page 请求服务端第page页的数据
     * @param isCache 判断是否需要缓存，如果是刷新可能需要缓存，如果是加载则不需要
     * @param callbackJson 从服务端回调回来的数据，进行逻辑处理。
     */
    public static void requestRawData(final int page, boolean isCache ,
                                      final CallbackJson callbackJson) {
        DBManager dbManager = new DBManager();
        final String url = WpPostInterface.BASE_URL + "get_posts/?page=" + page;

        queryCachedData(dbManager, url, callbackJson);
        if (!NetworkUtils.isNetworkAvailable(BaseApplication.getContext()) && isCache) {
            callbackJson.onFailure("网络不可用");
            return;
        }
        requestData(dbManager, page, url, isCache, callbackJson);
    }

    /**
     * 查找已缓存的数据
     * @param dbManager 用来查询数据
     * @param url 通过url来查询
     * @param callbackJson 回调
     */
    private static void queryCachedData(DBManager dbManager, String url, CallbackJson callbackJson) {
        String json = dbManager.getData(url);
        if (!"".equals(json)) {
            List<Post> posts = PostsWithStatus.parseJson(json).getPosts();
            callbackJson.onSuccess(posts);
        }
    }

    /**
     * 向服务端请求数据
     * @param dbManager 用来插入数据到数据库
     * @param page 服务端请求第page页
     * @param url 服务端请求的url
     * @param isCache 是否需要缓存
     * @param callbackJson 回调方法
     */
    private static void requestData(final DBManager dbManager, int page, final String url,
                                    final boolean isCache, final CallbackJson callbackJson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WpPostInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WpPostInterface wpPostInterface = retrofit.create(WpPostInterface.class);
        Call<ResponseBody> call = wpPostInterface.getRawPostsByPage(page);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    if (isCache) {
                        dbManager.insertData(url, json);
                    }

                    List<Post> posts = PostsWithStatus.parseJson(json).getPosts();
                    callbackJson.onSuccess(posts);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
