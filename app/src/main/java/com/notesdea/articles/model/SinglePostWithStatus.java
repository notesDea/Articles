package com.notesdea.articles.model;

/**
 * Created by notes on 2016/12/6.
 */
//一篇文章和状态
public class SinglePostWithStatus {
    private String status;
    private Post post;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
