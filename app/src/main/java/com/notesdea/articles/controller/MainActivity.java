package com.notesdea.articles.controller;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.notesdea.articles.model.LoadOnScrollListener;
import com.notesdea.articles.R;
import com.notesdea.articles.model.HomeAdapter;
import com.notesdea.articles.model.NetworkUtils;
import com.notesdea.articles.model.Post;
import com.notesdea.articles.model.PostsWithStatus;
import com.notesdea.articles.model.WpPostInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //设置刷新和加载数据视图
        mRefreshLayout.setOnRefreshListener(this);
        mRecycler.addOnScrollListener(new LoadOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRefreshLayout.setRefreshing(true);
        refreshData(1);

    }

    //初始化视图
    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_articles_item);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        //添加适配器
        mAdapter = new HomeAdapter(mPosts, MainActivity.this);
        mRecycler.setAdapter(mAdapter);
        //添加分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, mLinearLayoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
    }

    //加载数据
    private void refreshData(final int page) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(WpPostInterface.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    WpPostInterface wpPostInterface = retrofit.create(WpPostInterface.class);
                    Call<PostsWithStatus> call = wpPostInterface.getPostsByPage(page);
                    try {
                        PostsWithStatus postsWithStatus = call.execute().body();
                        mTotalPages = postsWithStatus.getPages();
                        mPosts.addAll(postsWithStatus.getPosts());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }).start();
        }
    }

    //处理下拉刷新的逻辑
    @Override
    public void onRefresh() {
        //todo 执行刷新逻辑
        mRefreshLayout.setRefreshing(false);
    }

    //加载更多数据
    private void loadMore() {
        if (mCurrentPage < mTotalPages) {
            refreshData(++mCurrentPage);
        }
    }
}
