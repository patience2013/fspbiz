package com.fangshang.fspbiz.fragment.housing.build;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 11:19
 */

public class AuditInfoFragment extends BaseFragment {
    @BindView(R.id.tv_committime)
    TextView mTv_committime;
    @BindView(R.id.tv_audit_status)
    TextView mTv_audit_status;
    @BindView(R.id.tv_audit_time)
    TextView mTv_audit_time;
    @BindView(R.id.tv_issue)
    TextView mTv_issue;
    @BindView(R.id.lin_nopassreason)
    LinearLayout mLin_nopassreason;
    @BindView(R.id.lin_update_time)
    LinearLayout mLin_update_time;
    @BindView(R.id.lin_cuishen)
    LinearLayout mLin_cuishen;
    @BindView(R.id.tv_update_time)
    TextView mTv_update_time;
    @BindView(R.id.tv_cuishen_time)
    TextView mTv_cuishen_time;


    private HttpResponseStruct.BuildDetail buildDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_audit_info;
    }
    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            buildDetail= (HttpResponseStruct.BuildDetail) bundle.getSerializable("buildDetail");
        }
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        if(null!=buildDetail){
            //提交时间
            mTv_committime.setText(ZhuanHuanUtil.getTimes(buildDetail.approve.createTime+""));
            //审核状态
            if(buildDetail.approve.approveSta==0){
                mTv_audit_status.setText("未审核");
            }else if(buildDetail.approve.approveSta==1){
                mTv_audit_status.setText("审核通过");
                mLin_nopassreason.setVisibility(View.GONE);
                mLin_update_time.setVisibility(View.GONE);
                mLin_cuishen.setVisibility(View.GONE);
            }else if(buildDetail.approve.approveSta==2){
                mTv_audit_status.setText("审核中");
                mLin_nopassreason.setVisibility(View.GONE);
                mLin_update_time.setVisibility(View.GONE);
                mLin_cuishen.setVisibility(View.VISIBLE);
            }else if(buildDetail.approve.approveSta==3){
                mTv_audit_status.setText("审核失败");
                mLin_nopassreason.setVisibility(View.VISIBLE);
                mLin_update_time.setVisibility(View.VISIBLE);
                mLin_cuishen.setVisibility(View.GONE);
            }
            //审核时间
            mTv_audit_time.setText(null==buildDetail.approve.approveTime? "无":ZhuanHuanUtil.getTimes(buildDetail.approve.approveTime));
            //失败原因
            mTv_issue.setText(null==buildDetail.approve.approveIssue?"无":buildDetail.approve.approveIssue);
            //修改时间
            mTv_update_time.setText(null==buildDetail.approve.updateTime?"无修改记录":ZhuanHuanUtil.getTimes(buildDetail.approve.updateTime+""));
            //催审时间
            mTv_cuishen_time.setText(null==buildDetail.approve.updateTime?"催审时间没字段":"催审时间没字段");
        }
    }
}
