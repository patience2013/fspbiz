package com.fangshang.fspbiz.fragment.me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.fragment.me.activity.CertificationDetailActivity;
import com.fangshang.fspbiz.fragment.me.activity.MeInfoActivity;
import com.fangshang.fspbiz.fragment.me.activity.SetActivity;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
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
 * Created by xiong on 2017/12/16/016 17:20
 */

public class MeFragment extends BaseFragment{
    @BindView(R.id.tv_mobile)
    TextView mTv_mobile;
    @BindView(R.id.tv_certification)
    TextView mTv_certification;
    @BindView(R.id.img_status)
    ImageView mImg_status;
    @BindView(R.id.lin_certification)
    LinearLayout mLin_certification;

    Gson gson =new Gson();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        StatusBarUtil.setColor((Activity) mContext,getResources().getColor(R.color.transparent));
//        StatusBarUtil.setTransparent(getActivity());
        mTv_mobile.setText(AccountHelper.getUser().getPhone());
        if(AccountHelper.getUser().getBussType()==3){
            mLin_certification.setVisibility(View.GONE);
        }else{
            mLin_certification.setVisibility(View.VISIBLE);

        }

        ggetUserIdentityById();
    }
    @OnClick({R.id.lin_info,R.id.tv_set,R.id.lin_allorder,R.id.lin_reservation,R.id.lin_tel,R.id.lin_opinion,R.id.lin_certification})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.lin_info:
                MeInfoActivity.actionStart(mContext);
                break;
            case R.id.tv_set:
                SetActivity.actionStart(mContext);
                break;
            case R.id.lin_allorder:
                TsUtils.show("全部订单");
                break;
            case R.id.lin_reservation:
                TsUtils.show("我的预约");
                break;
            case R.id.lin_tel:
                TsUtils.show("客服电话");
                break;
            case R.id.lin_opinion:
                TsUtils.show("意见反馈");
                break;
            case R.id.lin_certification:
                CertificationDetailActivity.actionStart(mContext);

                break;

        }
    }
    public void ggetUserIdentityById(){
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(final String sign) {
                HttpRequestStruct.UserIdentityDetail userIdentityDetail =new HttpRequestStruct.UserIdentityDetail();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android",AccountHelper.getUser().getToken(),sign,date+"",number);
                userIdentityDetail.msgReq =msgReqWithToken;
                String json = gson.toJson(userIdentityDetail);
                OkGo.<BaseBean<HttpResponseStruct.UserIdentityData>>post(InterfaceUrl.GET_USERIDENTITY)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.UserIdentityData>>((Activity) mContext) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserIdentityData>> response) {
                                Logger.d("    token    "+AccountHelper.getUser().getToken()+"    date    "+date+"    number    "+number+"    sign    "+sign);
                                Logger.d("code"+response.body().getResultCode()+"msg"+response.body().getResultMsg());
                                if("00000".equals(response.body().getResultCode())){
                                    if(response.body().getData().userIdentityDetail.isAuthentication==0){
                                        mTv_certification.setText("未认证");
                                        mImg_status.setImageResource(R.drawable.certification_1);
                                    }else if(response.body().getData().userIdentityDetail.isAuthentication==1){
                                        mTv_certification.setText("审核通过");
                                        mImg_status.setImageResource(R.drawable.certification_4);

                                    }else if(response.body().getData().userIdentityDetail.isAuthentication==2){
                                        mTv_certification.setText("审核中");
                                        mImg_status.setImageResource(R.drawable.certification_3);
                                    }else if(response.body().getData().userIdentityDetail.isAuthentication==3){
                                        mTv_certification.setText("审核失败");
                                        mImg_status.setImageResource(R.drawable.certification_2);
                                    }
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
