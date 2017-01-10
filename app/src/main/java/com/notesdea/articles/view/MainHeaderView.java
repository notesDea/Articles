package com.notesdea.articles.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.notesdea.articles.R;
import com.notesdea.articles.model.refreshload.view.SwipeRefreshLayout;

/**
 * Created by notesdea on 1/9/17.
 */

public class MainHeaderView extends SwipeRefreshLayout {
    private static final String TAG = MainHeaderView.class.getSimpleName();

    private TextView mTextRefresh;
    private int mHeaderHeight;

    public MainHeaderView(Context context) {
        this(context, null);
    }

    public MainHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.height_header_view);
        mTextRefresh = (TextView) findViewById(R.id.text_refresh);
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onMove(int y, boolean automatic) {
        if (y >= mHeaderHeight) {
            mTextRefresh.setText("释放刷新");
        } else {
            mTextRefresh.setText("下拉刷新");
        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        super.onRefresh();
        mTextRefresh.setText("正在刷新");
    }
}
