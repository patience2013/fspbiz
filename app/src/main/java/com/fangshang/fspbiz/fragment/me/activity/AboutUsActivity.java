package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;

/**
 * Created by xiong on 2018/1/6/006 17:08
 */

public class AboutUsActivity extends BaseBackActivity {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initView() {
        setTopTitle("关于我们");
    }
}
