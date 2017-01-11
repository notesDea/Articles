package com.notesdea.articles.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.notesdea.articles.R;
import com.notesdea.articles.model.refreshload.view.SwipeRefreshLayout;

/**
 * Created by notesdea on 1/9/17.
 */

public class MainHeaderView extends SwipeRefreshLayout {

    private static final String TAG = MainHeaderView.class.getSimpleName();
    //整个 HeaderView 的高度
    private int mHeaderHeight;
    //正在刷新时显示的进度条
    private ProgressBar mProgressBar;
    //箭头图片
    private ImageView mImageArrow;
    //释放时箭头的动画（向上）
    private Animation mArrowRelease;
    //下拉时箭头的动画（向下）
    private Animation mArrowSwipe;
    //判断是否已旋转
    private boolean mRotated;

    public MainHeaderView(Context context) {
        this(context, null);
    }

    public MainHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.height_header_view);
        mArrowRelease = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        mArrowSwipe = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mImageArrow = (ImageView) findViewById(R.id.image_pull_arrow);
    }

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            mProgressBar.setVisibility(GONE);
            mImageArrow.setVisibility(VISIBLE);

            if (y >= mHeaderHeight) {
                if (!mRotated) {
                    mImageArrow.clearAnimation();
                    mImageArrow.startAnimation(mArrowRelease);
                    mRotated = true;
                }
            } else {
                if (mRotated) {
                    mImageArrow.clearAnimation();
                    mImageArrow.startAnimation(mArrowSwipe);
                    mRotated = false;
                }
            }
        }
    }

    @Override
    public void onComplete() {
        mImageArrow.clearAnimation();
        mImageArrow.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        mRotated = false;
        mImageArrow.clearAnimation();
        mImageArrow.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
    }
}
