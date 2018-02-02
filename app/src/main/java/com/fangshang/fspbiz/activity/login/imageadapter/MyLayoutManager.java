package com.fangshang.fspbiz.activity.login.imageadapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public class MyLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;
    public MyLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyLayoutManager(Context context) {
        super(context);
    }
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
        }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}