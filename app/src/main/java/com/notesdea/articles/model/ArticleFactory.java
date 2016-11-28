package com.notesdea.articles.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by notesdea on 11/18/16.
 */

public class ArticleFactory {
    public static List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        Article article = new Article("是命中注定", "2015-07-93");
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        articles.add(article);
        return articles;
    }
}
