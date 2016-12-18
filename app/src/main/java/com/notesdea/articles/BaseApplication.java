package com.notesdea.articles;

import android.app.Application;
import android.content.Context;

/**
 * Created by notes on 2016/12/18.
 */

public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    //获取全局的 Context
    public static Context getContext() {
        return mContext;
    }
}
