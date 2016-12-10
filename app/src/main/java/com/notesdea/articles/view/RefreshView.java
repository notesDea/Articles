//package com.notesdea.articles.view;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.notesdea.articles.R;
//
///**
// * Created by notesdea on 11/22/16.
// */
//
//public class RefreshView extends LinearLayout implements View.OnTouchListener {
//
//    //更新完成状态或未进行更新状态
//    public static final int STATUS_REFRESH_FINISHED = 0;
//    //正在更新状态
//    public static final int STATUS_REFRESHING = 1;
//    //下拉刷新状态
//    public static final int STATUS_PULL_REFRESH = 2;
//    //释放更新状态
//    public static final int STATUS_RELEASE_REFRESH = 3;
//    //当前的刷新状态
//    private int mCurrentStatus = STATUS_REFRESH_FINISHED;
//    //上一次的刷新状态
//    private int mLastStatus = mCurrentStatus;
//
//    //下拉头部滚动的速度
//    public static final int SCROLL_SPEED = -50;
//    //隐藏的头部视图的高度
//    private int mHideHeaderHeight;
//    //头部视图的布局参数
//    private MarginLayoutParams mHeaderLayoutParams;
//    //下拉时触发更新的临界值
//    private int mTouchSlop;
//    //是否是第一次加载
//    private boolean mLoadOnce;
//
//    //头部刷新视图
//    private View mHeader;
//    //箭头图片
//    private ImageView mImageArrow;
//    //头部刷新时的描述文字
//    private TextView mTextDescription;
//    //更新时的进度条
//    private ProgressBar mProgressBar;
//    //整个 ListView 对象
//    private ListView mListView;
//
//    //触摸时按下时的坐标
//    private float mYDown;
//    //判断是否可下拉
//    private boolean mAbleToPull;
//    // 下拉刷新的回调接口
//    private PullToRefreshListener mListener;
//
//    public RefreshView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mHeader = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, null, true);
//        mImageArrow = (ImageView) mHeader.findViewById(R.id.image_arrow);
//        mTextDescription = (TextView) mHeader.findViewById(R.id.text_description);
//        mProgressBar = (ProgressBar) mHeader.findViewById(R.id.progress_bar);
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        setOrientation(VERTICAL);
//        addView(mHeader, 0);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);
//        if (changed && !mLoadOnce) {
//            mHideHeaderHeight = -mHeader.getHeight();
//            mHeaderLayoutParams = (MarginLayoutParams) mHeader.getLayoutParams();
//            mHeaderLayoutParams.topMargin = mHideHeaderHeight;
//            //获取  ListView
//            mListView = (ListView) getChildAt(1);
//            mListView.setOnTouchListener(this);
//            mLoadOnce = true;
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        isAbleToPull(event);
//        if (mAbleToPull) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mYDown =  event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float yMove = event.getRawY();
//                    int distance = (int) (yMove - mYDown);
//                    if (distance <= 0 && mHeaderLayoutParams.topMargin <= mHideHeaderHeight ||
//                            distance < mTouchSlop) {
//                        return false;
//                    }
//
//                    offsetHeader((distance / 2) + mHideHeaderHeight);
//                    break;
//                case MotionEvent.ACTION_UP:
//                default:
//                    executeTask();
//                    break;
//            }
//            //下拉的同时让 ListView 失clockclcl去焦点
//            if (mCurrentStatus == STATUS_PULL_REFRESH ||
//                    mCurrentStatus == STATUS_RELEASE_REFRESH) {
//                updateHeaderView();
//                makeViewFocusInvalid(mListView);
//                mLastStatus = mCurrentStatus;
//                return true;
//            }
//        }
//        return false;
//    }
//
//    //判断是否可进行下拉操作
//    private void isAbleToPull(MotionEvent event) {
//        View childView = mListView.getChildAt(0);
//        if (childView != null) {
//            int firstVisiblePos = mListView.getFirstVisiblePosition();
//            if (childView.getTop() == 0 && firstVisiblePos == 0) {
//                if (!mAbleToPull) {
//                    mYDown = event.getRawY();
//                }
//                mAbleToPull = true;
//            } else {
//                if (mHeaderLayoutParams.topMargin != mHideHeaderHeight) {
//                    mHeaderLayoutParams.topMargin = mHideHeaderHeight;
//                    mHeader.setLayoutParams(mHeaderLayoutParams);
//                }
//                mAbleToPull = false;
//            }
//        } else {
//            mAbleToPull = true;
//        }
//    }
//
//    //设置 mHeader 的 TopMargin 属性
//    private void setHeaderTopMargin(int topMargin) {
//        mHeaderLayoutParams.topMargin = topMargin;
//        mHeader.setLayoutParams(mHeaderLayoutParams);
//    }
//
//    //偏移下拉头
//    private void offsetHeader(int offset) {
//        if (mCurrentStatus != STATUS_REFRESHING) {
//            if (mHeaderLayoutParams.topMargin > 0) {
//                mCurrentStatus = STATUS_RELEASE_REFRESH;
//            } else {
//                mCurrentStatus = STATUS_PULL_REFRESH;
//            }
//            //偏移下拉头
//            setHeaderTopMargin(offset);
//        }
//    }
//
//    //判断状态执行相应的任务。刷新或隐藏任务
//    private void executeTask() {
//        if (mCurrentStatus == STATUS_RELEASE_REFRESH) {
//            // 松手时如果是释放立即刷新状态，就去调用正在刷新的任务
//            new RefreshingTask().execute();
//        } else if (mCurrentStatus == STATUS_PULL_REFRESH) {
//            // 松手时如果是下拉状态，就去调用隐藏下拉头的任务
//            new HideHeaderTask().execute();
//        }
//    }
//
//    //更新头部视图
//    private void updateHeaderView() {
//        if (mCurrentStatus != mLastStatus) {
//            if (mCurrentStatus == STATUS_PULL_REFRESH) {
//                mImageArrow.setVisibility(VISIBLE);
//                rotateArrow();
//                mTextDescription.setText(R.string.pull_to_refresh);
//                mProgressBar.setVisibility(GONE);
//            } else if (mCurrentStatus == STATUS_RELEASE_REFRESH) {
//                mImageArrow.setVisibility(VISIBLE);
//                rotateArrow();
//                mTextDescription.setText(R.string.release_to_refresh);
//                mProgressBar.setVisibility(GONE);
//            } else if (mCurrentStatus == STATUS_REFRESHING) {
//                mProgressBar.setVisibility(VISIBLE);
//                mTextDescription.setText(R.string.refreshing);
//                mImageArrow.clearAnimation();
//                mImageArrow.setVisibility(GONE);
//            }
//        }
//    }
//
//    //使视图的点击和距焦失效
//    public void makeViewFocusInvalid(View view) {
//        view.setFocusable(false);
//        view.setFocusableInTouchMode(false);
//        view.setPressed(false);
//    }
//
//    //旋转箭头
//    private void rotateArrow() {
//        float pivotX = mImageArrow.getWidth() / 2;
//        float pivotY = mImageArrow.getHeight() / 2;
//        float fromDegrees = 0;
//        float toDegrees = 0;
//        if (mCurrentStatus == STATUS_PULL_REFRESH) {
//            fromDegrees = 180f;
//            toDegrees = 360f;
//        } else if (mCurrentStatus == STATUS_RELEASE_REFRESH) {
//            fromDegrees = 0f;
//            toDegrees = 180f;
//        }
//        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
//        animation.setDuration(100);
//        animation.setFillAfter(true);
//        mImageArrow.startAnimation(animation);
//    }
//
//    public void setOnRefreshListener(PullToRefreshListener listener) {
//        mListener = listener;
//    }
//
//    //结束刷新
//    public void finishRefreshing() {
//        mCurrentStatus = STATUS_REFRESH_FINISHED;
//        new HideHeaderTask().execute();
//    }
//
//    //刷新的异步任务
//    class RefreshingTask extends AsyncTask<Void, Integer, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            int topMargin = mHeaderLayoutParams.topMargin;
//            while (true) {
//                topMargin += SCROLL_SPEED;
//                if (topMargin <= 0) {
//                    break;
//                }
//
//                publishProgress(topMargin);
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            mCurrentStatus = STATUS_REFRESHING;
//            publishProgress(0);
//            if (mListener != null) {
//                mListener.onRefresh();
//            }
//            return null;
//        }
//
//        //更新头部视图和设置  topMargin。
//        @Override
//        protected void onProgressUpdate(Integer... topMargin) {
//            updateHeaderView();
//            setHeaderTopMargin(topMargin[0]);
//        }
//    }
//
//    //隐藏头部的异步任务
//    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {
//        @Override
//        protected Integer doInBackground(Void... values) {
//            int topMargin = mHeaderLayoutParams.topMargin;
//            while (true) {
//                topMargin += SCROLL_SPEED;
//                if (topMargin <= mHideHeaderHeight) {
//                    topMargin = mHideHeaderHeight;
//                    break;
//                }
//                publishProgress(topMargin);
//                try {
//                    Thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return topMargin;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... topMargin) {
//            setHeaderTopMargin(topMargin[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Integer topMargin) {
//            setHeaderTopMargin(topMargin);
//            mCurrentStatus = STATUS_REFRESH_FINISHED;
//        }
//    }
//
//    //下拉刷新监听事件
//    public interface PullToRefreshListener {
//        //在刷新时的回调方法
//        void onRefresh();
//    }
//}
