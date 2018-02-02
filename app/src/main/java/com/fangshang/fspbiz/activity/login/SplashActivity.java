package com.fangshang.fspbiz.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;


import com.fangshang.fspbiz.MainActivity;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.LoginActivity;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.Config;


/**
 * Created by littleSpider on 2017/9/25.
 */

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("firstTime", MODE_PRIVATE);
        final boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        if (Config.getInstance().getBoolean(Config.FIRST_LANUCHER, true)) {
                            intent.setClass(SplashActivity.this, WelcomeActivity.class);
                        } else {
                            if(AccountHelper.getUser().getPhone()!=null && !"".equals(AccountHelper.getUser().getToken())){
                                intent.setClass(SplashActivity.this, MainActivity.class);
                            }else{
                                intent.setClass(SplashActivity.this, LoginActivity.class);
                            }

                        }
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }, 2000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
