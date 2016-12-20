package com.notesdea.articles.model;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
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
public class RequestManager {

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
     * @param isRefreshing 判断是否需要缓存，如果是刷新可能需要缓存，如果是加载则不需要
     * @param callbackJson 从服务端回调回来的数据，进行逻辑处理。
     */
    public static void requestRawData(int page, boolean isRefreshing ,
                                      CallbackJson callbackJson) {
        DBManager dbManager = new DBManager();
        String url = WpPostInterface.BASE_URL + "get_posts/?page=" + page;

        if (isRefreshing) {
            String cachedJson = queryCachedData(dbManager, url, callbackJson);
            if (!RequestManager.isNetworkAvailable(BaseApplication.getContext())) {
                callbackJson.onFailure("网络不可用");
                return;
            }
            requestData(dbManager, page, url, cachedJson, callbackJson);
        } else {
            if (!RequestManager.isNetworkAvailable(BaseApplication.getContext())) {
                queryCachedData(dbManager, url, callbackJson);
            } else {
                requestData(dbManager, page, url, null, callbackJson);
            }
        }

    }

    /**
     * 查找已缓存的数据
     * @param dbManager 用来查询数据
     * @param url 通过url来查询
     * @param callbackJson 回调
     */
    private static String queryCachedData(DBManager dbManager, String url, CallbackJson callbackJson) {
        String json = dbManager.getData(url);
        if (!"".equals(json)) {
            List<Post> posts = PostsWithStatus.parseJson(json).getPosts();
            callbackJson.onSuccess(posts);
        }
        return json;
    }

    /**
     * 向服务端请求数据
     * @param dbManager 用来插入数据到数据库
     * @param page 服务端请求第page页
     * @param url 服务端请求的url
     * @param cachedJson 与已缓存的数据比较，如果有改变则代替旧数据。如果为 #null 而请求新数据
     * @param callbackJson 回调方法
     */
    private static void requestData(final DBManager dbManager, int page, final String url,
                                    @Nullable final String cachedJson, final CallbackJson callbackJson) {
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
                    String json;
                    if (Build.VERSION.SDK_INT >= 24) {
                        json = Html.fromHtml(response.body().string(),
                                Html.FROM_HTML_MODE_LEGACY).toString();
                    } else {
                        json = Html.fromHtml(response.body().string()).toString();
                    }
                    if (cachedJson == null || !cachedJson.equals(json)) {
                        dbManager.insertData(url, json);
                        List<Post> posts = PostsWithStatus.parseJson(json).getPosts();
                        callbackJson.onSuccess(posts);
                    }

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
