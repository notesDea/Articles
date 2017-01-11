package com.notesdea.articles.model.refreshload.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.notesdea.articles.model.refreshload.SwipeRefreshTrigger;
import com.notesdea.articles.model.refreshload.SwipeTrigger;

/**
 * Created by notesdea on 1/9/17.
 */

//刷新的布局，想实现刷新功能可以继承这个类
public class SwipeRefreshLayout extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {

    public SwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onReset() {

    }
}