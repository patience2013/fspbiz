package com.fangshang.fspbiz.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.RegexUtils;
import com.fangshang.fspbiz.MainActivity;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.CertificationActivity;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.interfaceUrl.JsonCallBack;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.TsUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/3/003 10:11
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone_number)
    EditText mEt_phone_number;
    @BindView(R.id.et_password)
    EditText mEt_password;
    @BindView(R.id.iv_clear_phone)
    ImageView mIv_clear_phone;
    Gson gson =new Gson();
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
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
        if(!"".equals(AccountHelper.getUser().getPhone())){
            mEt_phone_number.setText(AccountHelper.getUser().getPhone());
        }
    }
    @OnClick({R.id.tv_register,R.id.tv_forget_password,R.id.btn_login,R.id.iv_clear_phone})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_register:
                RegisterActivity.actionStart(this);
                break;
            case R.id.tv_forget_password:
                ForgetPassActivity.actionStart(this);
                break;
            case R.id.btn_login:
                if ("".equals(getUserName())){
                    TsUtils.show("用户名不能为空");
                    return;
                }
                //判断用户名是否符合手机号码规则
                if (!RegexUtils.isMobileSimple(getUserName())){
                    TsUtils.show("用户名格式错误");
                    return;
                }
                //判断密码是否为空
                if ("".equals(getPassword())){
                    TsUtils.show("密码不能为空");
                    return;
                }
//                HttpUtil.getSignature(this,new Date().getTime()+"");
                final long date = new Date().getTime();
                final String number=HttpUtil.getRandom();
                HttpUtil.getSignature(LoginActivity.this, date + "",number, new HttpUtil.NetListener() {
                    @Override
                    public void success(String sign) {
                        HttpRequestStruct.Login getCode = new HttpRequestStruct.Login();
                        getCode.msgReq = new HttpRequestStruct.MsgReq("android", sign, date + "", number);
                        getCode.userName = getUserName();
                        getCode.password =getPassword();
                        String json = gson.toJson(getCode);
                        OkGo.<BaseBean<HttpResponseStruct.LoginData>>post(InterfaceUrl.LOGIN)
                                .tag(this)
                                .upJson(json)
                                .execute(new JsonCallBack<BaseBean<HttpResponseStruct.LoginData>>() {
                                    @Override
                                    public void onSuccess(Response<BaseBean<HttpResponseStruct.LoginData>> response) {
                                        //检测用户是否存在
                                        if ("10010".equals(response.body().getResultCode())) {
                                            AccountHelper.login(response.body().getData().user);
                                            Logger.d(response.body().getData().user.getToken());
//                                            Logger.d(date);
//                                            Logger.d(number);

//                                            //是否认证 个人不需要认证
//                                            if(AccountHelper.getUser().getBussType()!=3){
//                                                //审核状态为未审核时才跳转到认证页面
//                                                if(AccountHelper.getUser().getIsAuthentication()==0){
//                                                    CertificationActivity.actionStart(LoginActivity.this,AccountHelper.getUser().getBussType(),null);
//                                                }
//                                            }else{
                                                finish();
                                                MainActivity.actionStart(LoginActivity.this);
//                                            }

                                        } else {
                                            TsUtils.show(response.body().getResultMsg());
                                        }
                                    }
                                });
                    }
                });

                break;
            case R.id.iv_clear_phone:
                mEt_phone_number.setText("");
                break;
        }
    }
    /**
     * 根据返回值获取用户名
     * @return
     */
    private String getUserName(){
        String uerName = mEt_phone_number.getText().toString();
        return uerName;
    }

    /**
     * 获取密码输入框内容
     * @return
     */
    private String getPassword(){
        return mEt_password.getText().toString();
    }
}
