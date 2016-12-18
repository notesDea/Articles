package com.notesdea.articles.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notesdea.articles.ItemClickListener;
import com.notesdea.articles.R;
import com.notesdea.articles.model.CallbackJson;
import com.notesdea.articles.model.HomeAdapter;
import com.notesdea.articles.model.LoadOnScrollListener;
import com.notesdea.articles.model.NetworkUtils;
import com.notesdea.articles.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/10.
 */

public class MainFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainFragment";
    
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
    private int mPage;
    //是否是刷新
    private boolean mIsRefresh;

    //判断是否是首次加载
    private boolean mOnceLoad;

    private OnSwitchFragmentListener mReplaceListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mReplaceListener = (OnSwitchFragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnceLoad = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initView(root);
        initListener();
        return root;
    }

    //初始化视图
    private void initView(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_articles_item);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLinearLayoutManager);
        //添加适配器
        mAdapter = new HomeAdapter(mPosts, getActivity());
        mRecycler.setAdapter(mAdapter);
        //添加分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                getActivity(), mLinearLayoutManager.getOrientation());
        mRecycler.addItemDecoration(dividerItemDecoration);
    }

    //初始化监听事件
    private void initListener() {
        //设置刷新和加载数据视图
        mRefreshLayout.setOnRefreshListener(this);
        mRecycler.addOnScrollListener(new LoadOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMore();
                //回来执行的 todo loadMore可否不用？ 记录笔记
            }
        });
        mRecycler.addOnItemTouchListener(new ItemClickListener(new ItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post post = mPosts.get(position);
                mReplaceListener.onSwitchDetailFragment(post.getTitle(), post.getContent());
            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mOnceLoad) {
            mRefreshLayout.setRefreshing(true);
            refreshData(true);
            mOnceLoad = false;
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        //todo 添加刷新数据的逻辑
        mRefreshLayout.setRefreshing(false);
    }

    //加载更多数据
    private void loadMore() {
        refreshData(false);
    }

    /**
     * 刷新数据
     * @param isRefresh 判断是否是刷新状态
     */
    private void refreshData(final boolean isRefresh) {
         //获取原始数据
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            NetworkUtils.requestRawData(mPage, isRefresh, new CallbackJson<List<Post>>() {

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
                    mRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    //切换 Fragment 监听器
    public interface OnSwitchFragmentListener {
        //切换到 DetailFragment
        void onSwitchDetailFragment(String title, String content);
    }
}
