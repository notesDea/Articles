package com.notesdea.articles.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.notesdea.articles.ItemClickListener;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private FragmentManager mFragmentManager;
    private DetailFragment mDetailFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initView();
        mFragmentManager = getFragmentManager();
        mDetailFragment = new DetailFragment();

//        //设置刷新和加载数据视图
//        mRefreshLayout.setOnRefreshListener(this);
//        mRecycler.addOnScrollListener(new LoadOnScrollListener() {
//            @Override
//            public void onLoadMore() {
//                loadMore();
//            }
//        });
//        mRecycler.addOnItemTouchListener(new ItemClickListener(mRecycler, new ItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_main_detail, mDetailFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        }));
    }

//
//    //初始化视图
//    private void initView() {
//        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
//
//        mRecycler = (RecyclerView) findViewById(R.id.recycler_articles_item);
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        mRecycler.setLayoutManager(mLinearLayoutManager);
//        //添加适配器
//        mAdapter = new HomeAdapter(mPosts, MainActivity.this);
//        mRecycler.setAdapter(mAdapter);
//        //添加分隔线
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
//                this, mLinearLayoutManager.getOrientation());
//        mRecycler.addItemDecoration(dividerItemDecoration);
//    }
//
//    //加载数据
//    private void refreshData(final int page) {
//        if (NetworkUtils.isNetworkAvailable(this)) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(WpPostInterface.BASE_URL)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    WpPostInterface wpPostInterface = retrofit.create(WpPostInterface.class);
//                    Call<PostsWithStatus> call = wpPostInterface.getPostsByPage(page);
//                    try {
//                        PostsWithStatus postsWithStatus = call.execute().body();
//                        mTotalPages = postsWithStatus.getPages();
//                        mPosts.addAll(postsWithStatus.getPosts());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.notifyDataSetChanged();
//                            mRefreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//            }).start();
//        }
//    }
//
//    //处理下拉刷新的逻辑
//    @Override
//    public void onRefresh() {
//        //todo 执行刷新逻辑
//        mRefreshLayout.setRefreshing(false);
//    }
//
//    //加载更多数据
//    private void loadMore() {
//        if (mCurrentPage < mTotalPages) {
//            refreshData(++mCurrentPage);
//        }
//    }
}
