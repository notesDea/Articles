package com.notesdea.articles.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.notesdea.articles.R;
import com.notesdea.articles.model.refreshload.view.SwipeLoadMoreLayout;

/**
 * Created by notesdea on 1/9/17.
 */

public class MainFooterView extends SwipeLoadMoreLayout{
    private static final String TAG = MainFooterView.class.getSimpleName();

    private TextView mTextLoadMore;

    public MainFooterView(Context context) {
        this(context, null);
    }

    public MainFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextLoadMore = (TextView) findViewById(R.id.text_load_more);
    }
}
