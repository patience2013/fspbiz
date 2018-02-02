package com.fangshang.fspbiz.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by xiong on 2018/1/8/008 9:59
 */

public class NoSlideViewPager extends ViewPager {
    public NoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlideViewPager(Context context) {

        super(context);
    }

    //是否可以滑动
    private boolean isCanScroll = false;

    //----------禁止左右滑动------------------
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }
    //-------------------------------------------

    /**
     * 设置 是否可以滑动
     * @param isCanScroll
     */
    public void setScrollble(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}
