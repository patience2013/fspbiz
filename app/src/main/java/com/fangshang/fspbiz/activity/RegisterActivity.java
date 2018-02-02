package com.fangshang.fspbiz.activity;

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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.AgreementH5Activity;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.bean.User;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.interfaceUrl.JsonCallBack;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.MyDialog;
import com.fangshang.fspbiz.util.TsUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/3/003 14:41
 */

public class RegisterActivity extends BaseBackActivity {
    @BindView(R.id.et_phone_number)
    EditText mEt_phone_number;
    @BindView(R.id.et_password)
    EditText mEt_password;
    @BindView(R.id.et_code)
    EditText mEt_code;
    @BindView(R.id.iv_clear_phone)
    ImageView mIv_clear_phone;
    @BindView(R.id.tv_getCode)
    TextView mTv_getCode;
    @BindView(R.id.radio_group)
    RadioGroup mRadio_group;

    Gson gson = new Gson();
    Timer timer;
    int second = 60;
    int type=0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                second--;
                mTv_getCode.setText(second + "(s)");
                if (second <= 0) { //待读秒结束后,将按钮恢复初始状态,并关闭定时器
                    mTv_getCode.setText("获取验证码");
                    mTv_getCode.setEnabled(true);
//                    mTv_getCode.setBackgroundResource(R.drawable.selector_login_button);
                    timer.cancel();
                }
            }
        }
    };

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        setTopTitle("注册");
        mEt_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mIv_clear_phone.setVisibility(View.GONE);
                } else {
                    mIv_clear_phone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRadio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_one:
                        type=1;
                        break;
                    case R.id.rb_two:
                        type=2;
                        break;
                    case R.id.rb_three:
                        type=3;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_register, R.id.tv_login, R.id.tv_getCode,R.id.iv_clear_phone,R.id.tv_xieyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if ("".equals(getMobile())){
                    TsUtils.show("请填写你的手机号");
                    return;
                }
                //判断用户名是否符合手机号码规则
                if (!RegexUtils.isMobileSimple(getMobile())){
                    TsUtils.show("输入手机号有误");
                    return;
                }
                //判断密码是否为空
                if ("".equals(getCode())){
                    TsUtils.show("验证码不能为空");
                    return;
                }
                //判断密码是否为空
                if ("".equals(getPass())){
                    TsUtils.show("密码不能为空");
                    return;
                }
                if(type==0){
                    TsUtils.show("请选择用户类型");
                    return;
                }
                final long date = new Date().getTime();
                final String number=HttpUtil.getRandom();
                HttpUtil.getSignature(this, date + "",number, new HttpUtil.NetListener() {
                    @Override
                    public void success(String sign) {
                        HttpRequestStruct.Register getCode = new HttpRequestStruct.Register();
                        getCode.msgReq = new HttpRequestStruct.MsgReq("android", sign, date + "", number);
                        getCode.userName = getMobile();
                        getCode.password =getPass();
                        getCode.verifyCode =getCode();
                        getCode.userType =type;
                        String json = gson.toJson(getCode);
                        OkGo.<BaseBean<User>>post(InterfaceUrl.REGISTER)
                                .tag(this)
                                .upJson(json)
                                .execute(new JsonCallBack<BaseBean<User>>() {
                                    @Override
                                    public void onSuccess(Response<BaseBean<User>> response) {
                                        Logger.d(response.body().getResultCode());
                                        //检测用户是否存在
                                        if ("00000".equals(response.body().getResultCode())) {
                                            final MyDialog dialog = new MyDialog(RegisterActivity.this, "恭喜注册成功!", "即将跳转到登录页面");
                                            dialog.show();
                                            new CountDownTimer(1500, 1000) {
                                                public void onTick(long millisUntilFinished) {
                                                }

                                                public void onFinish() {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            }.start();
                                        } else {
                                            TsUtils.show(response.body().getResultMsg());
                                        }
                                    }
                                });
                    }
                });

                break;
            case R.id.tv_login:
//                LoginActivity.actionStart(this);
                finish();
                break;
            case R.id.tv_getCode:
                checkMobile();
                break;
            case R.id.iv_clear_phone:
                mEt_phone_number.setText("");
                break;
            case R.id.tv_xieyi:
                AgreementH5Activity.actionStart(RegisterActivity.this);
                break;
        }
    }

    public void checkMobile() {
        if ("".equals(getMobile())) {
            TsUtils.show("用户名不能为空");
            return;
        }
        //判断用户名是否符合手机号码规则
        if (!RegexUtils.isMobileSimple(getMobile())) {
            TsUtils.show("用户名格式错误");
            return;
        }
        final long date = new Date().getTime();
        final String number=HttpUtil.getRandom();
        HttpUtil.getSignature(this, date + "",number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.CheckMobile checkMobile = new HttpRequestStruct.CheckMobile();
                checkMobile.msgReq = new HttpRequestStruct.MsgReq("android", sign, date + "", number);
                checkMobile.userName = getMobile();
                String json = gson.toJson(checkMobile);
                OkGo.<BaseBean<HttpResponseStruct.CheckBoolean>>post(InterfaceUrl.CHECK_MOBILE)
                        .tag(this)
                        .upJson(json)
                        .execute(new JsonCallBack<BaseBean<HttpResponseStruct.CheckBoolean>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.CheckBoolean>> response) {
                                Logger.d(response.body().getData().userExist);
                                //检测用户是否存在
                                if (response.body().getData().userExist == true) {
                                    TsUtils.show("用户已存在，请直接登录");
                                } else {
                                    getVerificationCode();
                                }
                            }
                        });
            }
        });

    }
    public void getVerificationCode(){
        final long date = new Date().getTime();
        final String number=HttpUtil.getRandom();
        HttpUtil.getSignature(this, date + "",number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.GetCode getCode = new HttpRequestStruct.GetCode();
                getCode.msgReq = new HttpRequestStruct.MsgReq("android", sign, date + "", number);
                getCode.userName = getMobile();
                String json = gson.toJson(getCode);
                OkGo.<BaseBean<Object>>post(InterfaceUrl.GET_CODE)
                        .tag(this)
                        .upJson(json)
                        .execute(new JsonCallBack<BaseBean<Object>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<Object>> response) {
                                Logger.d(response.body().getResultCode());
                                //检测用户是否存在
                                if ("00000".equals(response.body().getResultCode())) {
                                    TsUtils.show("已发送");
                                    changeButtonStatus();
                                } else {
                                    TsUtils.show(response.body().getResultMsg());
                                }
                            }
                        });
            }
        });
    }

    public String getMobile() {
        return mEt_phone_number.getText().toString();
    }
    public String getPass() {
        return mEt_password.getText().toString();
    }
    public String getCode() {
        return mEt_code.getText().toString();
    }

    /**
     * 改变发送验证码按钮状态
     */
    private void changeButtonStatus() {
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
        }, 0, 1000);
        //将获取验证码的按钮设置为不可点击的状态
        mTv_getCode.setEnabled(false);
//        mTv_getCode.setBackgroundResource(R.drawable.shape_button_gray);
    }

}
