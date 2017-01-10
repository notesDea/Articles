package com.notesdea.articles.model.refreshload.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.notesdea.articles.R;
import com.notesdea.articles.model.refreshload.OnLoadMoreListener;
import com.notesdea.articles.model.refreshload.OnRefreshListener;
import com.notesdea.articles.model.refreshload.SwipeLoadMoreTrigger;
import com.notesdea.articles.model.refreshload.SwipeRefreshTrigger;
import com.notesdea.articles.model.refreshload.SwipeTrigger;

/**
 * Created by notesdea on 1/9/17.
 */

//负责完成下拉刷新和上拉加载的类.
public class SwipeToLoadLayout extends LinearLayout {

    private static final String TAG = SwipeToLoadLayout.class.getSimpleName();

    private static final float DEFAULT_DRAG_RATIO = 0.5f;

    private static final int DEFAULT_DEFAULT_TO_REFRESHING_DURATION = 300;
    private static final int DEFAULT_SWIPING_REFRESH_TO_DEFAULT_DURATION = 300;
    private static final int DEFAULT_RELEASE_REFRESH_TO_REFRESHING_DURATION = 200;
    private static final int DEFAULT_REFRESHING_TO_DEFAULT_DURATION = 300;

    private static final int DEFAULT_DEFAULT_TO_LOADING_MORE_DURATION = 300;
    private static final int DEFAULT_SWIPING_LOAD_MORE_TO_DEFAULT_DURATION = 300;
    private static final int DEFAULT_RELEASE_LOAD_MORE_TO_LOADING_MORE_DURATION = 200;
    private static final int DEFAULT_LOADING_MORE_TO_DEFAULT_DURATION = 300;

    //默认状态
    public static final int STATUS_DEFAULT = 0;
    //下拉准备刷新状态
    public static final int STATUS_REFRESH_SWIPING = 1;
    //释放刷新状态
    public static final int STATUS_REFRESH_RELEASE = 2;
    //正在刷新中状态
    public static final int STATUS_REFRESHING = 3;
    //上拉准备加载状态
    public static final int STATUS_LOAD_MORE_SWIPING = -1;
    //释放刷新状态
    public static final int STATUS_LOAD_MORE_RELEASE = -2;
    //正在加载中状态
    public static final int STATUS_LOADING_MORE = -3;

    //自动下拉刷新滚动的时间
    private int mDefault2RefreshingDuration = DEFAULT_DEFAULT_TO_REFRESHING_DURATION;
    //从刷新状态（未经过释放）回到默认状态的时间
    private int mSwipingRefresh2DefaultDuration = DEFAULT_SWIPING_REFRESH_TO_DEFAULT_DURATION;
    //刷新时释放滚动到 HeaderView 原始高度的时间
    private int mReleaseRefresh2RefreshingDuration = DEFAULT_RELEASE_REFRESH_TO_REFRESHING_DURATION;
    //下拉刷新完成后, HeaderView滚动到不可见的时间
    private int mRefreshing2DefaultDuration = DEFAULT_REFRESHING_TO_DEFAULT_DURATION;

    //自动上拉加载时向上滚动的速度
    private int mDefault2LoadingMoreDuration = DEFAULT_DEFAULT_TO_LOADING_MORE_DURATION;
    //从加载状态（未经过释放）回到默认状态的时间
    private int mSwipingLoadMore2DefaultDuration = DEFAULT_SWIPING_LOAD_MORE_TO_DEFAULT_DURATION;
    //释放加载后, 恢复到加载View原始状态的滚动时间
    private int mReleaseLoadMore2LoadingMoreDuration = DEFAULT_RELEASE_LOAD_MORE_TO_LOADING_MORE_DURATION;
    //加载完成后往下滚动的时间
    private int mLoadingMore2DefaultDuration = DEFAULT_LOADING_MORE_TO_DEFAULT_DURATION;

    //是否启用刷新
    private boolean mRefreshEnabled;
    //是否启用加载
    private boolean mLoadMoreEnabled;
    //拖动的比例
    private float mDragRatio;
    //触发刷新动作的偏移量
    private float mRefreshTriggerOffset;
    //触发加载动作的偏移量
    private float mLoadMoreTriggerOffset;

    //Touch的移动距离大于这个值才算移动
    private int mTouchSlop;

    //代表下拉, 上拉的状态, 见STATUS
    private int mStatus = STATUS_DEFAULT;
    //顶部的刷新视图
    private View mHeaderView;
    //底部的加载视图
    private View mFooterView;
    //显示内容的视图
    private View mTargetView;
    //HeaderView 的高度
    private int mHeaderHeight;
    //FooterView 的高度
    private int mFooterHeight;
    //如果有 HeaderView 返回 true
    private boolean mHasHeaderView;
    //如果有 FooterView 返回 true
    private boolean mHasFooterView;
    //HeaderView 的偏移量
    private int mHeaderOffset;
    //FooterView 的偏移量
    private int mFooterOffset;
    //中间显示内容的View 的偏移量
    private int mTargetOffset;

    private float mLastX;
    private float mLastY;
    private float mInitDownX;
    private float mInitDownY;
    //如果是自动下拉则是 true
    private boolean mAutoSwiping;

    //刷新监听器, 通过 setOnRefreshListener() 设置值
    private OnRefreshListener mRefreshListener;
    //加载监听器, 通过 setOnLoadMoreListener() 设置值
    private OnLoadMoreListener mLoadMoreListener;

    private AutoScroller mAutoScroller;

    public SwipeToLoadLayout(Context context) {
        this(context, null, 0);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeToLoadLayout, defStyleAttr, 0);
        try {
            mDragRatio = a.getFloat(R.styleable.SwipeToLoadLayout_drag_ratio, DEFAULT_DRAG_RATIO);
            //刷新相关初始化
            mRefreshEnabled = a.getBoolean(R.styleable.SwipeToLoadLayout_refresh_enabled, true);
            mRefreshTriggerOffset = a.getDimensionPixelOffset(
                    R.styleable.SwipeToLoadLayout_refresh_trigger_offset, 0);
            mDefault2RefreshingDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_default_to_refreshing_duration,
                    DEFAULT_DEFAULT_TO_REFRESHING_DURATION);
            mSwipingRefresh2DefaultDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_swiping_refresh_to_default_duration,
                    DEFAULT_SWIPING_REFRESH_TO_DEFAULT_DURATION);
            mReleaseRefresh2RefreshingDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_release_refresh_to_refreshing_duration,
                    DEFAULT_RELEASE_REFRESH_TO_REFRESHING_DURATION);
            mRefreshing2DefaultDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_refreshing_to_default_duration,
                    DEFAULT_REFRESHING_TO_DEFAULT_DURATION);
            //加载相关初始化
            mLoadMoreEnabled = a.getBoolean(R.styleable.SwipeToLoadLayout_load_more_enabled, true);
            mLoadMoreTriggerOffset = a.getDimensionPixelOffset(
                    R.styleable.SwipeToLoadLayout_load_more_trigger_offset, 0);
            mDefault2LoadingMoreDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_default_to_loading_more_duration,
                    DEFAULT_DEFAULT_TO_LOADING_MORE_DURATION);
            mSwipingLoadMore2DefaultDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_swiping_load_more_to_default_duration,
                    DEFAULT_SWIPING_LOAD_MORE_TO_DEFAULT_DURATION);
            mReleaseLoadMore2LoadingMoreDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_release_load_more_to_loading_more_duration,
                    DEFAULT_RELEASE_LOAD_MORE_TO_LOADING_MORE_DURATION);
            mLoadingMore2DefaultDuration = a.getInt(
                    R.styleable.SwipeToLoadLayout_loading_more_to_default_duration,
                    DEFAULT_LOADING_MORE_TO_DEFAULT_DURATION);
        } finally {
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mAutoScroller = new AutoScroller();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        } else if (childCount > 0 && childCount < 4) {
            mHeaderView = findViewById(R.id.swipe_refresh_header);
            mFooterView = findViewById(R.id.swipe_load_more_footer);
            mTargetView = findViewById(R.id.swipe_target);
        } else {
            throw new IllegalStateException("Child views must equsls or less than 4");
        }

        //先隐藏Header/FooterView
        if (mTargetView == null)  return;
        if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
            mHeaderView.setVisibility(GONE);
        }
        if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
            mFooterView.setVisibility(GONE);
        }
    }

    //测量View的高度, 并设置偏移量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            View headerView = mHeaderView;
            measureChildWithMargins(headerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams params = (MarginLayoutParams) headerView.getLayoutParams();
            mHeaderHeight = headerView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (mRefreshTriggerOffset < mHeaderHeight) {
                mRefreshTriggerOffset = mHeaderHeight;
            }
        }

        if (mTargetView != null) {
            View targetView = mTargetView;
            measureChildWithMargins(targetView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }

        if (mFooterView != null) {
            View footerView = mFooterView;
            measureChildWithMargins(footerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams params = (MarginLayoutParams) footerView.getLayoutParams();
            mFooterHeight = footerView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (mLoadMoreTriggerOffset < mFooterHeight) {
                mLoadMoreTriggerOffset = mFooterHeight;
            }
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        layoutChildren();
        mHasHeaderView = (mHeaderView != null);
        mHasFooterView = (mFooterView != null);
    }

    //动态设置子 View 的布局位置
    private void layoutChildren() {
        int layoutHeight = getMeasuredHeight();
        int layoutPaddingLeft = getPaddingLeft();
        int layoutPaddingTop = getPaddingTop();

        if (mTargetView == null) return;

        if (mHeaderView != null) {
            View headerView = mHeaderView;
            MarginLayoutParams params = (MarginLayoutParams) headerView.getLayoutParams();
            int left = layoutPaddingLeft + params.leftMargin;
            int top = layoutPaddingTop + params.topMargin + mHeaderOffset - mHeaderHeight;
            int right = left + headerView.getMeasuredWidth();
            int bottom = top + headerView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }

        if (mTargetView != null) {
            View targetView = mTargetView;
            MarginLayoutParams params = (MarginLayoutParams) targetView.getLayoutParams();
            int left = layoutPaddingLeft + params.leftMargin;
            int top = layoutPaddingTop + mTargetOffset;
            int right = left + targetView.getMeasuredWidth();
            int bottom = top + targetView.getMeasuredHeight();
            mTargetView.layout(left, top, right, bottom);
        }

        if (mFooterView != null) {
            View footerView = mFooterView;
            MarginLayoutParams params = (MarginLayoutParams) footerView.getLayoutParams();
            int left = layoutPaddingLeft + params.leftMargin;
            int bottom = layoutHeight -
                    layoutPaddingLeft - params.bottomMargin + mFooterHeight + mFooterOffset;
            int top = bottom - footerView.getMeasuredHeight();
            int right = left + footerView.getMeasuredWidth();
            mFooterView.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onActionPointerUp();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitDownX = mLastX = ev.getX();
                mInitDownY = mLastY = ev.getY();

                //如果已经处在准备刷新加载的过程中, 直接拦截
                if (mStatus == STATUS_REFRESH_SWIPING || mStatus == STATUS_REFRESH_RELEASE ||
                        mStatus == STATUS_LOAD_MORE_SWIPING || mStatus == STATUS_LOAD_MORE_RELEASE) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float dx = moveX - mInitDownX;
                float dy = moveY - mInitDownY;
                mLastX = moveX;
                mLastY = moveY;

                boolean moved = Math.abs(dx) < Math.abs(dy);
                if ((dy > 0 && moved && isCanRefresh()) ||
                        (dy < 0 && moved && isCanLoadMore())) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = moveX - mLastX;
                float dy = moveY - mLastY;
                mLastX = moveX;
                mLastY = moveY;

                if (Math.abs(dx) > Math.abs(dy) || Math.abs(dx) > mTouchSlop) {
                    return false;
                }

                if (mStatus == STATUS_DEFAULT) {
                    if (dy > 0 && isCanRefresh()) {
                        mRefreshCallback.onPrepare();
                        setStatus(STATUS_REFRESH_SWIPING);
                    } else if (dy < 0 && isCanLoadMore()) {
                        mLoadMoreCallback.onPrepare();
                        setStatus(STATUS_LOAD_MORE_SWIPING);
                    }
                } else if (isRefreshStatus() && mTargetOffset < 0) {
                    setStatus(STATUS_DEFAULT);
                    fixCurrentLayout();
                    return false;
                } else if (isLoadMoreStatus() && mTargetOffset > 0) {
                    setStatus(STATUS_DEFAULT);
                    fixCurrentLayout();
                    return false;
                }
                setStatusByDy(dy);
                return true;
        }
        return super.onTouchEvent(event);
    }

    //如果可以触发刷新, 返回 true
    private boolean isCanRefresh() {
        int scrollUp = -1;
        boolean canScrollUp = ViewCompat.canScrollVertically(mTargetView, scrollUp);
        return mRefreshEnabled && mHeaderView != null && mRefreshTriggerOffset > 0 && !canScrollUp;
    }

    //如果可以触发加载, 返回 true
    private boolean isCanLoadMore() {
        int scrollDown = 1;
        boolean canScrollDown = ViewCompat.canScrollVertically(mTargetView, scrollDown);
        return mLoadMoreEnabled && mFooterView != null && mLoadMoreTriggerOffset > 0 && !canScrollDown;
    }

    //根据当前的状态修正视图布局
    private void fixCurrentLayout() {
        switch (mStatus) {
            case STATUS_REFRESHING:
                mTargetOffset = (int) (mRefreshTriggerOffset + 0.5f);
                mHeaderOffset = mTargetOffset;
                mFooterOffset = 0;
                layoutChildren();
                invalidate();
                break;
            case STATUS_LOADING_MORE:
                mTargetOffset = -(int) (mLoadMoreTriggerOffset + 0.5f);
                mHeaderOffset = 0;
                mFooterOffset = mTargetOffset;
                layoutChildren();
                invalidate();
                break;
            case STATUS_DEFAULT:
                mTargetOffset = 0;
                mHeaderOffset = 0;
                mFooterOffset = 0;
                layoutChildren();
                invalidate();
                break;
        }
    }

    //通过偏移量dy 设置状态并滚动
    private void setStatusByDy(float dy) {
        if (mStatus == STATUS_REFRESH_SWIPING || mStatus == STATUS_REFRESH_RELEASE) {
            if (mTargetOffset >= mRefreshTriggerOffset) {
                setStatus(STATUS_REFRESH_RELEASE);
            } else {
                setStatus(STATUS_REFRESH_SWIPING);
            }
            fingerScrolling(dy);
        } else if (mStatus == STATUS_LOAD_MORE_SWIPING || mStatus == STATUS_LOAD_MORE_RELEASE) {
            if (-mTargetOffset >= mLoadMoreTriggerOffset) {
                setStatus(STATUS_LOAD_MORE_RELEASE);
            } else {
                setStatus(STATUS_LOAD_MORE_SWIPING);
            }
            fingerScrolling(dy);
        }
    }

    //设置状态
    private void setStatus(int status) {
        this.mStatus = status;
    }

    private void fingerScrolling(float dy) {
        float scrollY = dy * mDragRatio;
        if (isRefreshStatus()) {
            mRefreshCallback.onMove(mTargetOffset, false);
        } else if (isLoadMoreStatus()) {
            mLoadMoreCallback.onMove(mTargetOffset, false);
        }
        updateScroll(scrollY);

    }

    //如果是下拉刷新相关的状态就返回 true
    private boolean isRefreshStatus() {
        return mStatus > STATUS_DEFAULT;
    }

    //如果是上拉加载相关的状态就返回 true
    private boolean isLoadMoreStatus() {
        return mStatus < STATUS_DEFAULT;
    }

    //在触摸抬起时滚动回去
    private void onActionPointerUp() {
        switch (mStatus) {
            case STATUS_REFRESH_SWIPING:
                scrollSwipingRefresh2Default();
                break;
            case STATUS_REFRESH_RELEASE:
                mRefreshCallback.onRelease();
                scrollReleaseRefresh2Refreshing();
                break;
            case STATUS_LOAD_MORE_SWIPING:
                scrollSwipingLoadMore2Default();
                break;
            case STATUS_LOAD_MORE_RELEASE:
                mLoadMoreCallback.onRelease();
                scrollReleaseLoadMore2LoadingMore();
        }
    }

    private void onScrollFinished() {
        switch (mStatus) {
            case STATUS_REFRESH_SWIPING:
                if (mAutoSwiping) {
                    setStatus(STATUS_REFRESHING);
                    fixCurrentLayout();
                    mRefreshCallback.onRefresh();
                } else {
                    setStatus(STATUS_DEFAULT);
                    fixCurrentLayout();
                    mRefreshCallback.onReset();
                }
                break;
            case STATUS_REFRESH_RELEASE:
                setStatus(STATUS_REFRESHING);
                fixCurrentLayout();
                mRefreshCallback.onRefresh();
                break;
            case STATUS_REFRESHING:
                setStatus(STATUS_DEFAULT);
                fixCurrentLayout();
                mRefreshCallback.onReset();
                break;

            case STATUS_LOAD_MORE_SWIPING:
                if (mAutoSwiping) {
                    setStatus(STATUS_LOADING_MORE);
                    fixCurrentLayout();
                    mLoadMoreCallback.onLoadMore();
                } else {
                    setStatus(STATUS_DEFAULT);
                    fixCurrentLayout();
                    mLoadMoreCallback.onReset();
                }
                break;
            case STATUS_LOAD_MORE_RELEASE:
                setStatus(STATUS_LOADING_MORE);
                fixCurrentLayout();
                mLoadMoreCallback.onLoadMore();
                break;
            case STATUS_LOADING_MORE:
                setStatus(STATUS_DEFAULT);
                fixCurrentLayout();
                mLoadMoreCallback.onReset();
                break;
        }
    }

    ////////////////////////////////////刷新相关的滚动//////////////////////////////////////////////
    //滚动：从下拉刷新到默认状态
    private void scrollSwipingRefresh2Default() {
        mAutoScroller.autoScroll(-mHeaderOffset, mSwipingRefresh2DefaultDuration);
    }

    //滚动：从释放刷新到正在刷新状态
    private void scrollReleaseRefresh2Refreshing() {
        mAutoScroller.autoScroll(mHeaderHeight - mHeaderOffset, mReleaseRefresh2RefreshingDuration);
    }

    //滚动：从正在刷新到默认状态
    private void scrollRefreshing2Default() {
        mAutoScroller.autoScroll(-mHeaderOffset, mRefreshing2DefaultDuration);
    }

    //滚动：从默认到正在刷新状态
    private void scrollDefault2Refreshing() {
        mAutoScroller.autoScroll((int) (mRefreshTriggerOffset + 0.5), mDefault2RefreshingDuration);
    }

    ////////////////////////////////////加载相关的滚动//////////////////////////////////////////////
    //滚动：从上拉加载到默认状态
    private void scrollSwipingLoadMore2Default() {
        mAutoScroller.autoScroll(-mFooterOffset, mSwipingLoadMore2DefaultDuration);
    }

    //滚动：从释放加载到正在加载状态
    private void scrollReleaseLoadMore2LoadingMore() {
        mAutoScroller.autoScroll(-mFooterOffset - mFooterHeight, mReleaseLoadMore2LoadingMoreDuration);
    }

    //滚动：从正在加载到默认状态
    private void scrollLoadingMore2Default() {
        mAutoScroller.autoScroll(-mFooterOffset, mLoadingMore2DefaultDuration);
    }

    //滚动：从默认到正在加载状态
    private void scrollDefault2LoadingMore() {
        mAutoScroller.autoScroll((int) (mLoadMoreTriggerOffset + 0.5), mDefault2LoadingMoreDuration);
    }

    /**
     *
     * @param dy 垂直滚动的距离, 同时回调 onMove() 方法
     */
    private void autoScrolling(int dy) {
        switch (mStatus) {
            case STATUS_REFRESH_SWIPING:
                mRefreshCallback.onMove(mTargetOffset, false);
                break;
            case STATUS_REFRESH_RELEASE:
                mRefreshCallback.onMove(mTargetOffset, false);
                break;
            case STATUS_REFRESHING:
                mRefreshCallback.onMove(mTargetOffset, false);
                break;
            case STATUS_LOAD_MORE_SWIPING:
                mLoadMoreCallback.onMove(mTargetOffset, false);
                break;
            case STATUS_LOAD_MORE_RELEASE:
                mLoadMoreCallback.onMove(mTargetOffset, false);
                break;
            case STATUS_LOADING_MORE:
                mLoadMoreCallback.onMove(mTargetOffset, false);
                break;
        }
        updateScroll(dy);
    }

    /**
     *
     * @param dy 垂直滚动的距离
     */
    private void updateScroll(float dy) {
        if (dy == 0) {
            return;
        }
        mTargetOffset += dy;
        if (isRefreshStatus()) {
            mHeaderOffset = mTargetOffset;
            mFooterOffset = 0;
        } else if (isLoadMoreStatus()) {
            mHeaderOffset = 0;
            mFooterOffset = mTargetOffset;
        }
        layoutChildren();
        invalidate();
    }

    //设置刷新监听器
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.mRefreshListener = refreshListener;
    }

    //设置加载监听器
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    //设置刷新, 如果为 false 时停止刷新动画. 如果为 true 时自动进入刷新状态
    public void setRefreshing(boolean autoSwiping) {
        mAutoSwiping = autoSwiping;
        if (autoSwiping) {
            setStatus(STATUS_REFRESH_SWIPING);
            scrollDefault2Refreshing();
        } else {
            //状态设置在 onScrollFinished() 里完成
            if (mStatus == STATUS_REFRESHING) {
                scrollRefreshing2Default();
            }
        }

    }

    public void setLoadingMore(boolean isAutoLoadingMore) {
        if (isAutoLoadingMore) {
            setStatus(STATUS_LOAD_MORE_SWIPING);
            scrollDefault2LoadingMore();
        } else {
            if (mStatus == STATUS_LOADING_MORE) {
                scrollLoadingMore2Default();
            }
        }
    }

    RefreshCallback mRefreshCallback = new RefreshCallback() {
        @Override
        public void onPrepare() {
            if (mHeaderView != null && mStatus == STATUS_DEFAULT) {
                mHeaderView.setVisibility(VISIBLE);
                ((SwipeTrigger) mHeaderView).onPrepare();
            }
        }

        @Override
        public void onMove(int y, boolean automatic) {
            if (mHeaderView != null) {
                if (mHeaderView.getVisibility() != VISIBLE) {
                    mHeaderView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mHeaderView).onMove(y, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mHeaderView != null) {
                ((SwipeTrigger) mHeaderView).onRelease();
            }
        }

        @Override
        public void onRefresh() {
            if (mHeaderView != null && mStatus == STATUS_REFRESHING) {
                if (mHeaderView instanceof SwipeRefreshTrigger) {
                    ((SwipeRefreshTrigger) mHeaderView).onRefresh();
                }
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        }

        @Override
        public void onReset() {
            if (mHeaderView != null) {
                mHeaderView.setVisibility(GONE);
                ((SwipeTrigger) mHeaderView).onReset();
            }
        }
    };

    LoadMoreCallback mLoadMoreCallback = new LoadMoreCallback() {
        @Override
        public void onPrepare() {
            if (mFooterView != null && mStatus == STATUS_DEFAULT) {
                mFooterView.setVisibility(VISIBLE);
                ((SwipeTrigger) mFooterView).onPrepare();
            }
        }

        @Override
        public void onMove(int y, boolean automatic) {
            if (mFooterView != null) {
                if (mFooterView.getVisibility() != VISIBLE) {
                    mFooterView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mFooterView).onMove(y, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mFooterView != null) {
                ((SwipeTrigger) mFooterView).onRelease();
            }
        }

        @Override
        public void onLoadMore() {
            if (mFooterView != null && mStatus == STATUS_LOADING_MORE) {
                if (mFooterView instanceof SwipeLoadMoreTrigger) {
                    ((SwipeLoadMoreTrigger) mFooterView).onLoadMore();
                }
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }

        @Override
        public void onReset() {
            if (mFooterView != null) {
                ((SwipeTrigger) mFooterView).onReset();
            }
        }
    };

    //刷新的回调方法
    abstract class RefreshCallback implements SwipeTrigger, SwipeRefreshTrigger {
    }

    //加载的回调方法
    abstract class LoadMoreCallback implements SwipeTrigger, SwipeLoadMoreTrigger {

    }

    private class AutoScroller implements Runnable {

        Scroller mScroller;
        int mLastY;

        public AutoScroller() {
            mScroller = new Scroller(getContext());
        }

        @Override
        public void run() {
            boolean isFinish = mScroller.isFinished() || !mScroller.computeScrollOffset();
            int currY = mScroller.getCurrY();
            int dy = currY - mLastY;
            if (isFinish) {
                finish();
            } else {
                mLastY = currY;
                SwipeToLoadLayout.this.autoScrolling(dy);
                post(this);
            }
        }

        public void finish() {
            mLastY = 0;
            removeCallbacks(this);
            onScrollFinished();

        }

        public void autoScroll(int dy, int duration) {
            removeCallbacks(this);
            mLastY = 0;
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, dy, duration);
            post(this);
        }
    }
}