package com.notesdea.articles.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.notesdea.articles.R;
import com.notesdea.articles.model.CallbackJson;
import com.notesdea.articles.model.HomeAdapter;
import com.notesdea.articles.model.OnItemClickListener;
import com.notesdea.articles.model.Post;
import com.notesdea.articles.model.RequestManager;
import com.notesdea.articles.model.refreshload.OnLoadMoreListener;
import com.notesdea.articles.model.refreshload.OnRefreshListener;
import com.notesdea.articles.model.refreshload.view.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnRefreshListener, OnLoadMoreListener {

    //工具栏
    private Toolbar mToolbar;
    //todo add annotation
    private SwipeToLoadLayout mSwipeToLoadLayout;
    //存储数据的视图
    private RecyclerView mRecycler;
    private LinearLayoutManager mLinearLayoutManager;
    //存储数据适配器
    private HomeAdapter mAdapter;
    //存储的文章
    private List<Post> mPosts = new ArrayList<>();
    //当前的页数
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    //初始化视图
    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        mSwipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mRecycler = (RecyclerView) findViewById(R.id.swipe_target);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        //添加适配器
        mAdapter = new HomeAdapter(mPosts, this);
        mRecycler.setAdapter(mAdapter);
        //添加分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, mLinearLayoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);

    }

    //初始化监听事件
    private void initListener() {
        //点击Item监听
        mRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post post = mPosts.get(position);
                Intent intent = new Intent(MainActivity.this, PostsActivity.class);
                intent.putExtra("title", post.getTitle())
                        .putExtra("content", post.getContent());
                startActivity(intent);
            }
        });
    }

    //在下拉刷新或是首次启动应用时调用
    @Override
    public void onRefresh() {
        mPage = 1;
        getData(true);
    }

    //在上拉加载数据时调用
    @Override
    public void onLoadMore() {
        getData(false);
    }

    /**
     * 获取数据（加载或刷新）
     * @param isRefresh 判断是否是刷新状态，如果为 false，则启用加载功能。
     */
    private void getData(final boolean isRefresh) {
        //获取原始数据
        RequestManager.requestRawData(mPage, isRefresh, new CallbackJson<List<Post>>() {

            @Override
            public void onSuccess(List<Post> result) {
                if (isRefresh) {
                    mPosts.clear();
                    mPage = 2;
                } else {
                    mPage++;
                }
                mPosts.addAll(result);
                mAdapter.notifyDataSetChanged();
                mSwipeToLoadLayout.setRefreshing(false);
                mSwipeToLoadLayout.setLoadingMore(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                mSwipeToLoadLayout.setRefreshing(false);
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
    }
}
