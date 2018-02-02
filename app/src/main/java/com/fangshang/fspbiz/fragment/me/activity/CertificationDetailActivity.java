package com.fangshang.fspbiz.fragment.me.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.CertificationActivity;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 17:08
 */

public class CertificationDetailActivity extends BaseBackActivity {
    @BindView(R.id.tv_status)
    TextView mTv_status;
    @BindView(R.id.img_status)
    ImageView mImg_status;
    @BindView(R.id.tv_goto_certification)
    TextView mTv_goto_certification;
    @BindView(R.id.tv_time)
    TextView mTv_time;
    @BindView(R.id.tv_reason)
    TextView mTv_reason;
    @BindView(R.id.tv_usertype)
    TextView mTv_usertype;
    @BindView(R.id.tv_companytype)
    TextView mTv_companytype;
    @BindView(R.id.tv_company_name)
    TextView mTv_company_name;
    @BindView(R.id.tv_address)
    TextView mTv_address;
    @BindView(R.id.tv_dockingname)
    TextView mTv_dockingname;
    @BindView(R.id.tv_dockingtel)
    TextView mTv_dockingtel;
    @BindView(R.id.lin_businesslicense)
    LinearLayout mLin_businesslicense;
    @BindView(R.id.img_businesslicense)
    ImageView mImg_businesslicense;
    @BindView(R.id.lin_area)
    LinearLayout mLin_area;
    @BindView(R.id.tv_area)
    TextView mTv_area;

    @BindView(R.id.tv_tips)
    TextView mTv_tips;


    @BindView(R.id.lin_company_public)
    LinearLayout mLin_company_public;

    HttpResponseStruct.UserIdentityDetail userIdentityDetail1;
    Gson gson = new Gson();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CertificationDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_certification_detail;
    }

    @Override
    protected void initView() {
        setTopTitle("认证详细信息");
        ggetUserIdentityById();
    }

    public void ggetUserIdentityById() {
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(CertificationDetailActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(final String sign) {
                HttpRequestStruct.UserIdentityDetail userIdentityDetail = new HttpRequestStruct.UserIdentityDetail();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken = new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(), sign, date + "", number);
                userIdentityDetail.msgReq = msgReqWithToken;
                String json = gson.toJson(userIdentityDetail);
                OkGo.<BaseBean<HttpResponseStruct.UserIdentityData>>post(InterfaceUrl.GET_USERIDENTITY)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.UserIdentityData>>(CertificationDetailActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserIdentityData>> response) {
                                Logger.d("    token    " + AccountHelper.getUser().getToken() + "    date    " + date + "    number    " + number + "    sign    " + sign);
                                Logger.d("code" + response.body().getResultCode() + "msg" + response.body().getResultMsg());
                                if ("00000".equals(response.body().getResultCode())) {
                                    userIdentityDetail1 = response.body().getData().userIdentityDetail;
                                    if (userIdentityDetail1.isAuthentication == 0) {
                                        mTv_status.setText("审核状态:   未认证");
                                        mImg_status.setImageResource(R.drawable.certification_1);
                                    } else if (userIdentityDetail1.isAuthentication == 1) {
                                        mTv_status.setText("审核状态:   已通过");
                                        mImg_status.setImageResource(R.drawable.certification_4);
                                        mTv_tips.setVisibility(View.VISIBLE);

                                    } else if (userIdentityDetail1.isAuthentication == 2) {
                                        mTv_status.setText("审核状态:   审核中");
                                        mImg_status.setImageResource(R.drawable.certification_3);
                                    } else if (userIdentityDetail1.isAuthentication == 3) {
                                        mTv_status.setText("审核状态:   未通过");
                                        mImg_status.setImageResource(R.drawable.certification_2);
                                        mTv_goto_certification.setVisibility(View.VISIBLE);
                                        mTv_reason.setVisibility(View.VISIBLE);
                                        mTv_reason.setText(userIdentityDetail1.approveIssue);
                                    }
                                    mTv_time.setText("提交时间:   " + ZhuanHuanUtil.getTimes(userIdentityDetail1.createTime) + "");

                                    if ("1".equals(userIdentityDetail1.userType)) {
                                        mTv_usertype.setText("公司");
                                    } else if ("2".equals(userIdentityDetail1.userType)) {
                                        mTv_usertype.setText("经纪人");
                                    } else if ("3".equals(userIdentityDetail1.userType)) {
                                        mTv_usertype.setText("个人");
                                    }
                                    if ("1".equals(userIdentityDetail1.enterpriseType)) {
                                        mTv_companytype.setText("企业招商");
                                        mLin_area.setVisibility(View.GONE);
                                    } else if ("2".equals(userIdentityDetail1.enterpriseType)) {
                                        mTv_companytype.setText("中介公司");
                                        mLin_area.setVisibility(View.VISIBLE);
                                    } else if ("3".equals(userIdentityDetail1.enterpriseType)) {
                                        mTv_companytype.setText("经纪人");
                                        mLin_area.setVisibility(View.VISIBLE);
                                        //隐藏公司公共信息和业务区域
                                        mLin_company_public.setVisibility(View.GONE);
                                        mLin_businesslicense.setVisibility(View.GONE);
                                    }
                                    mTv_company_name.setText(userIdentityDetail1.enterpriseName);//公司名称
                                    mTv_address.setText(userIdentityDetail1.enterpriseAddress);//公司地址
                                    mTv_dockingname.setText(userIdentityDetail1.contactPerson);//对接人
                                    mTv_dockingtel.setText(userIdentityDetail1.contactTele);//对接人手机
                                    Glide.with(CertificationDetailActivity.this).load(InterfaceUrl.IMG_HOST+userIdentityDetail1.enterpriseLicense).into(mImg_businesslicense);//营业执照
                                    mTv_area.setText(userIdentityDetail1.bussRegion);//业务区域

                                }
                            }
                        });
            }
        });
    }
    @OnClick(R.id.tv_goto_certification)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_goto_certification:
                CertificationActivity.actionStart(this,userIdentityDetail1.userType,userIdentityDetail1);
                break;
        }
    }

}
