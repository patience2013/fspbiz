package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;

import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 14:10
 */

public class NotificationSetActivity extends BaseBackActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NotificationSetActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_notificationset;
    }

    @Override
    protected void initView() {
        setTopTitle("新消息通知");
    }


}
