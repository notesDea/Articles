package com.notesdea.articles.controller;

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
import com.notesdea.articles.model.OnLoadMoreListener;
import com.notesdea.articles.model.OnSwitchFragmentListener;
import com.notesdea.articles.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by notes on 2016/12/10.
 */

public class MainFragment extends Fragment  implements
        SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, ItemClickListener.OnItemClickListener {

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
    private boolean mIsRefresh; //todo 用不到就删了

    //判断是否是首次加载
    private boolean mOnceLoad;
    //切换Fragment监听器
    private OnSwitchFragmentListener mSwitchListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSwitchListener = (OnSwitchFragmentListener) context;
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
        //刷新监听
        mRefreshLayout.setOnRefreshListener(this);
        //加载监听
        LoadOnScrollListener loadOnScrollListener = new LoadOnScrollListener();
        loadOnScrollListener.setLoadMoreListener(this);
        mRecycler.addOnScrollListener(loadOnScrollListener);
        //点击Item监听
        mRecycler.addOnItemTouchListener(new ItemClickListener(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mOnceLoad) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
            mOnceLoad = false;
        }
    }

    //在下拉刷新或是首次启动应用时调用
    @Override
    public void onRefresh() {
        mPage = 1;
        getData(true);
        mRefreshLayout.setRefreshing(false);
    }

    //在上拉加载数据时调用
    @Override
    public void onLoadMore() {
        getData(false);
    }

    //在点击Item时调用
    @Override
    public void onItemClick(View view, int position) {
        Post post = mPosts.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("title", post.getTitle());
        bundle.putString("content", post.getContent());
        mSwitchListener.onSwitchFragmentWithData(bundle);
    }

    /**
     * 获取数据（加载或刷新）
     * @param isRefresh 判断是否是刷新状态，如果为 false，则启用加载功能。
     */
    private void getData(final boolean isRefresh) {
         //获取原始数据 todo 不连网它会自动读取本地数据，所以不需要判断
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

    public void setOnSwitchFragmentListener(OnSwitchFragmentListener onSwitchFragmentListener) {
        this.mSwitchListener = onSwitchFragmentListener;
    }
}
