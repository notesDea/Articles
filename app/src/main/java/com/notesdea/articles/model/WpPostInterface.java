package com.notesdea.articles.model;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by notes on 2016/12/6.
 */

public interface WpPostInterface {
    //服务端的url
    String BASE_URL = "http://1.notesdea.applinzi.com/api/";
    //bundle 键值对
    String POST_TITLE = "title";
    String POST_DATE = "date";
    String POST_CONTENT = "content";
    String PAGE = "page";

    //请求获取所有文章
    @GET("get_posts/")
    Call<PostsWithStatus> getPosts();

    //请求获取某一页的所有文章
    @GET("get_posts/")
    Call<PostsWithStatus> getPostsByPage(@Query("page") int page);

    //请求获取某一页的原始数据
    @GET("get_posts/")
    Call<ResponseBody> getRawPostsByPage(@Query("page") int page);

    //通过id获取一条数据
    @GET("get_post/")
    Call<SinglePostWithStatus> getPostById(@Query("post_id") String id);


}
