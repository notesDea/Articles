package com.notesdea.articles;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.notesdea.articles.adapter.HomeAdapter;

/**
 * Created by notes on 2016/11/30.
 */

public abstract class LoadOnScrollListener extends RecyclerView.OnScrollListener {

    /**
    //可见的 item 个数
    private int mVisibleItemsCount;
    //总的 item 个数
    private int mTotalItemsCount;
    //可见的第一个 item 的位置
    private int mFirstVisiblePosition;
    //上一次的 item 个数
    private int mPreviousItemsCount = 0;
    //是否正在加载
    private boolean mLoading = true;
    */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        /**
         LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
         mVisibleItemsCount = recyclerView.getChildCount();
         mTotalItemsCount = layoutManager.getItemCount();
         mFirstVisiblePosition = layoutManager.findFirstVisibleItemPosition();

         if (mLoading) {

         }

         if (!mLoading && )
         */
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        Log.d("LoadOnScrollListener", "lastVisibleItem = " + lastVisibleItemPosition +
                ", totalItemCount = " + totalItemCount);
        if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                lastVisibleItemPosition == totalItemCount - 1) {
            onLoadMore();
        }

    }

    //回调方法，在加载更多时调用，需要在里面处理加载数据的逻辑
    public abstract void onLoadMore();
}
