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
    private int mFooterHeight;

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
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.height_footer_view);
        mTextLoadMore = (TextView) findViewById(R.id.text_load_more);
    }

    @Override
    public void onMove(int y, boolean automatic) {
        if (-y >= mFooterHeight) {
            mTextLoadMore.setText("释放加载");
        } else {
            mTextLoadMore.setText("上拉加载");
        }
    }

    @Override
    public void onLoadMore() {
        mTextLoadMore.setText("加载更多");
    }
}
