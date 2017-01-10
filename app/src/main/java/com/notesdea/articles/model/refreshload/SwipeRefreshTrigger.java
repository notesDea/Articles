package com.notesdea.articles.model.refreshload;

/**
 * Created by notesdea on 1/9/17.
 */

//触发刷新的接口
public interface SwipeRefreshTrigger {

    //在刷新时调用
    void onRefresh();
}
