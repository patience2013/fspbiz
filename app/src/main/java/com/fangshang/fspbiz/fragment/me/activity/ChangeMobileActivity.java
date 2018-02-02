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

public class ChangeMobileActivity extends BaseBackActivity {
    @BindView(R.id.et_phone_number)
    EditText mEt_phone_number;
    @BindView(R.id.iv_clear_phone)
    ImageView mIv_clear_phone;
    @BindView(R.id.tv_getCode)
    TextView mTv_getCode;

    Timer timer;
    int second = 60;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                second--;
                mTv_getCode.setText(second + "(s)");
                if (second <= 0){ //待读秒结束后,将按钮恢复初始状态,并关闭定时器
                    mTv_getCode.setText("获取验证码");
                    mTv_getCode.setEnabled(true);
//                    mTv_getCode.setBackgroundResource(R.drawable.selector_login_button);
                    timer.cancel();
                }
            }
        }
    };
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChangeMobileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_changemobile;
    }

    @Override
    protected void initView() {
        setTopTitle("更换手机号");
        mEt_phone_number.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.btn_submit,R.id.tv_getCode})
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
            case R.id.tv_getCode:
                changeButtonStatus();
                break;
        }
    }
    /**
     * 改变发送验证码按钮状态
     */
    private void changeButtonStatus(){
        second = 60;
        timer = new Timer();
        //使用定时器改变按钮上的文字内容
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.arg1 = 60;
                handler.sendMessage(message);
            }
        },0, 1000);
        //将获取验证码的按钮设置为不可点击的状态
        mTv_getCode.setEnabled(false);
//        mTv_getCode.setBackgroundResource(R.drawable.shape_button_gray);
    }
}
