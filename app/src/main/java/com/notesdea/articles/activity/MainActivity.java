package com.notesdea.articles.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.notesdea.articles.LoadOnScrollListener;
import com.notesdea.articles.R;
import com.notesdea.articles.adapter.HomeAdapter;
import com.notesdea.articles.model.Article;
import com.notesdea.articles.model.ArticleFactory;

import java.util.List;
import java.util.concurrent.ThreadFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //刷新视图
    private SwipeRefreshLayout mRefreshLayout;
    //存储数据的视图
    private RecyclerView mRecycler;
    //存储数据适配器
    private HomeAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    //数据
    private List<Article> mArticles = ArticleFactory.getArticles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        mRefreshLayout.setOnRefreshListener(this);
        mRecycler.addOnScrollListener(new LoadOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });

    }

    //初始化视图
    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_articles_item);
        mAdapter = new HomeAdapter(mArticles, this);
        mRecycler.setAdapter(mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        //添加分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, mLinearLayoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
    }

    //处理下拉刷新的逻辑
    @Override
    public void onRefresh() {
        //todo 执行刷新逻辑
        mRefreshLayout.setRefreshing(false);
    }

    //加载更多数据
    private void loadMore() {
        for (int i = 0; i < 10; i++) {
            mArticles.add(new Article("上拉加载出来的数据", "2016-11-30"));
        }
        mAdapter.notifyDataSetChanged();
    }
}
