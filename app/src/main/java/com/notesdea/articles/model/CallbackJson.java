package com.notesdea.articles.model;

/**
 * Created by notes on 2016/12/17.
 */

//回调回来，返回的已解析的数据
public interface CallbackJson<T> {
    //成功回调时调用，返回结果
    void onSuccess(T result);
}
