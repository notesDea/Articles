package com.notesdea.articles.model.refreshload;

/**
 * Created by notesdea on 1/9/17.
 */

//刷新时的监听器
public interface OnRefreshListener {
    //当正在刷新时调用
    void onRefresh();
}