package com.notesdea.articles.model;

/**
 * Created by notesdea on 11/18/16.
 */

public class Article {
    private String title;
    private String date;

    public Article() {
    }

    public Article(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

