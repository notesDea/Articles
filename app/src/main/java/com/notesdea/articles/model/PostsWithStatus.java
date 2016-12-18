package com.notesdea.articles.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/6.
 */
//多篇文章和它的状态及信息
public class PostsWithStatus {
    //状态
    private String status;
    //一页多少条数据
    private int count;
    //一共有多少条数据
    private int count_total;
    //页数
    private int pages;
    //所有的文章
    private List<Post> posts = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public static PostsWithStatus parseJson(String json) {
        return new Gson().fromJson(json, PostsWithStatus.class);
    }
}
