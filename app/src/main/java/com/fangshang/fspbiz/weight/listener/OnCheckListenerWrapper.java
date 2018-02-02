package com.fangshang.fspbiz.weight.listener;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * 防止Button的频繁点击,多次执行点击事件
 * <p/>
 *
 * @作者：JQ.Hu
 * @创建时间：2016/6/1 14:17
 */
public abstract class OnCheckListenerWrapper implements CheckBox.OnCheckedChangeListener {

    private static long lastClickTime;

    protected abstract void onSingleClick(View v);

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isFastDuplicateClick()) {
            return;
        }
        onSingleClick(buttonView);
    }


    /**
     * 是否为重复的快速点击
     *
     * @return
     */
    protected boolean isFastDuplicateClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            lastClickTime = time;
            return true;
        }
        return false;
    }
}
