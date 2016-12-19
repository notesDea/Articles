package com.notesdea.articles.model;

/**
 * Created by notes on 2016/12/19.
 */

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by notes on 2016/12/9.
 */

public abstract class OnItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    //是否是可点击状态
    private boolean mClickAble;
    //手势按下时的 X 坐标
    private float mLastPointX;
    //手势按下时的 Y 坐标
    private float mLastPointY;

    public OnItemClickListener() {
    }

    //在拦截事件中判断是否可点击，并且调用点击Item的方法
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastPointX = e.getX();
                mLastPointY = e.getY();
                mClickAble = true;
                break;
            case MotionEvent.ACTION_UP:
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mClickAble) {
                    onItemClick(childView, rv.getChildAdapterPosition(childView));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float pointX = e.getX();
                float pointY = e.getY();
                int distance = (int) computingDistance(mLastPointX, mLastPointY, pointX, pointY);
                if (distance != 0) {
                    mClickAble = false;
                }
        }

        return false;
    }

    /**
     * 计算两点间距离
     * @param startPointX 第一个点的 X 坐标
     * @param startPointY 第一个点的 Y 坐标
     * @param endPointX 第二个点的 X 坐标
     * @param endPointY 第三个点的 Y 坐标
     * @return 返回的是距离
     */
    public double computingDistance(float startPointX, float startPointY,
                                    float endPointX, float endPointY) {
        return Math.sqrt(pow(endPointX - startPointX) + pow(endPointY - startPointY));
    }

    /**
     * 计算 num 的平方
     * @param num 被平方的数字
     * @return 返回距离
     */
    private double pow(double num) {
        return Math.pow(num, 2);
    }


    /**
     * 在 Item 被点击时调用
     * @param view 被点击的视图
     * @param position 被点击的条目的位置
     */
     public abstract void onItemClick(View view, int position);


}
