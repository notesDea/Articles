package com.notesdea.articles.model.refreshload.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.notesdea.articles.model.refreshload.SwipeLoadMoreTrigger;
import com.notesdea.articles.model.refreshload.SwipeTrigger;

/**
 * Created by notesdea on 1/9/17.
 */

//加载的布局, 想实现加载可以继承这个布局
public class SwipeLoadMoreLayout extends FrameLayout implements SwipeTrigger, SwipeLoadMoreTrigger {
    public SwipeLoadMoreLayout(Context context) {
        this(context, null);
    }

    public SwipeLoadMoreLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLoadMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onLoadMore() {

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
