package com.notesdea.articles.model;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by notes on 2016/11/30.
 */

public class LoadOnScrollListener extends RecyclerView.OnScrollListener {

    private OnLoadMoreListener mLoadMoreListener;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();

        if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                lastCompletelyVisibleItemPosition == totalItemCount - 1) {
            mLoadMoreListener.onLoadMore();
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }
}
