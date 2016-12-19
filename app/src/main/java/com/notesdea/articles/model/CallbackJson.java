package com.notesdea.articles.model;

/**
 * Created by notes on 2016/12/17.
 */

//回调回来，返回已解析的数据
public interface CallbackJson<T> {
    //成功回调时调用，返回结果
    void onSuccess(T result);

    //在失败时调用，并有显示信息
    void onFailure(String message);
}
