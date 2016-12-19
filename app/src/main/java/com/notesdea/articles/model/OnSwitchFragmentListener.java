package com.notesdea.articles.model;

import android.os.Bundle;

/**
 * Created by notes on 2016/12/19.
 */

//切换 Fragment 监听器
public interface OnSwitchFragmentListener {
    //切换到 DetailFragment
    void onSwitchFragmentWithData(Bundle bundle);
}
