package com.notesdea.articles.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notesdea.articles.R;
import com.notesdea.articles.model.HomeAdapter;
import com.notesdea.articles.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/10.
 */

public class MainFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    //刷新视图
    private SwipeRefreshLayout mRefreshLayout;
    //存储数据的视图
    private RecyclerView mRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    //存储数据适配器
    private HomeAdapter mAdapter;
    //存储的文章
    private List<Post> mPosts = new ArrayList<>();
    //当前的页数
    private int mCurrentPage = 1;
    //服务端的总页数
    private int mTotalPages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRefreshLayout.setRefreshing(true);
        refreshData(1);
    }

    @Override
    public void onRefresh() {
//      todo 执行刷新逻辑
        mRefreshLayout.setRefreshing(false);
    }
}
