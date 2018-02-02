package com.fangshang.fspbiz.fragment.me.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.LoginActivity;
import com.fangshang.fspbiz.activity.login.imageadapter.SelectDialog;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.Config;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.TsUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 14:10
 */

public class SetActivity extends BaseBackActivity {
    @BindView(R.id.tv_loginout)
    TextView mTv_loginout;

    Gson gson = new Gson();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SetActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        setTopTitle("设置");
    }
    @OnClick({R.id.lin_accountpass,R.id.lin_message,R.id.lin_update,R.id.lin_aboutus,R.id.tv_loginout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_accountpass:
                AccountpassActivity.actionStart(this);
                break;
            case R.id.lin_message:
                NotificationSetActivity.actionStart(this);
                break;
            case R.id.lin_update:
                break;
            case R.id.lin_aboutus:
                AboutUsActivity.actionStart(this);
                break;
            case R.id.tv_loginout:
                List<String> names = new ArrayList<>();
                names.add("确定退出");
                SelectDialog selectDialog =new SelectDialog(this, R.style
                        .transparentFrameWindowStyle, new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position==0){
                            exitLogin();
                        }
                    }
                },names);
                selectDialog.show();
//                DialogHelper.getConfirmDialog(SetActivity.this, "你要退出登录吗？", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        exitLogin();
//                    }
//                }, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
                break;
        }
    }
    public void exitLogin(){
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(SetActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.UserIdentityDetail userIdentityDetail =new HttpRequestStruct.UserIdentityDetail();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken = new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(), sign, date + "", number);
                userIdentityDetail.msgReq =msgReqWithToken;
                String json = gson.toJson(userIdentityDetail);
                OkGo.<BaseBean<HttpResponseStruct.UserIdentity>>post(InterfaceUrl.LOGIN_OUT)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.UserIdentity>>(SetActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserIdentity>> response) {
//                                if("00000".equals(response.body().getResultCode())){
//                                    if(!response.body().getData().flag){
                                        Config.getInstance().set(Config.PASSWORD, "");
//                Config.getInstance().set(Config.USERNAME, "");
                                        Config.getInstance().set(Config.TOKEN, "");
                                        AccountHelper.clearUserCache();
//                int uuidtype = Config.getInstance().getInt(Config.UUID_TYPE, 1);
                                        AccountHelper.logout(mTv_loginout, new Runnable() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void run() {
                                                finish();
                                                Intent i = new Intent(SetActivity.this, LoginActivity.class);
                                                // set the new task and clear flags
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
//                        mSettingLineTop.setVisibility(View.INVISIBLE);
//                        mSettingLineBottom.setVisibility(View.INVISIBLE);
                                            }
                                        });
//                                    }else{
//                                        TsUtils.show("退出失败");
//                                    }
//                                }

                            }
                        });
            }
        });
    }
}
