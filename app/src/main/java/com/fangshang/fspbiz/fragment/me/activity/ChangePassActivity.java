package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.util.MyDialog;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/3/003 14:41
 */

public class ChangePassActivity extends BaseBackActivity {
    @BindView(R.id.et_oldpass)
    EditText mEt_oldpass;
    @BindView(R.id.iv_clear_phone)
    ImageView mIv_clear_phone;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChangePassActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_changepass;
    }

    @Override
    protected void initView() {
        setTopTitle("修改密码");
        mEt_oldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    mIv_clear_phone.setVisibility(View.GONE);
                }else{
                    mIv_clear_phone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_submit:
//                View view1 =View.inflate(this,R.layout.dialog_public,null);
//                DialogHelper.getConfirmDialog(this,"",view1,null).show();
                final MyDialog dialog =new MyDialog(this,"恭喜注册成功!","即将跳转到登录页面");
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
