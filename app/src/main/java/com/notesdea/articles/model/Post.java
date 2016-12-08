package com.notesdea.articles.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/6.
 */
//一篇文章的信息
public class Post {
    //一篇文章的id
    private int id;
    //一篇文章的标题
    private String title;
    //一篇文章的内容
    private String content;
    //一篇文章的地址
    private String url;
    //一篇发布的日期
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
