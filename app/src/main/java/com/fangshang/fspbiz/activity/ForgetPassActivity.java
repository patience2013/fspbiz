package com.fangshang.fspbiz.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.util.MyDialog;

import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/3/003 14:41
 */

public class ForgetPassActivity extends BaseBackActivity {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ForgetPassActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_forgetpass;
    }

    @Override
    protected void initView() {
        super.initView();
        setTopTitle("忘记密码");
    }

    @OnClick({R.id.tv_login,R.id.btn_reset})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_login:
//                LoginActivity.actionStart(this);
                finish();
                break;
            case R.id.btn_reset:
                final MyDialog dialog =new MyDialog(this,"密码已重置成功!","即将跳转到登录页面");
                dialog.show();
                new CountDownTimer(1500, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        dialog.dismiss();
                        finish();
                    }
                }.start();
                break;
        }
    }
}
