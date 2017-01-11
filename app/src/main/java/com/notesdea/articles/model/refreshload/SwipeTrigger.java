package com.notesdea.articles.model.refreshload;

/**
 * Created by notesdea on 1/9/17.
 */

//触发下拉或上拉的功能
public interface SwipeTrigger {

    //在准备阶段调用
    void onPrepare();

    /**
     * 在移动时调用
     * @param y 移动的距离
     * @param isComplete 判断是否已完成
     * @param automatic 是否要开启自动
     */
    void onMove(int y, boolean isComplete, boolean automatic);

    //完成释放后调用
    void onComplete();

    //在释放时调用
    void onRelease();

    //恢复到原来的状态时调用
    void onReset();
}
