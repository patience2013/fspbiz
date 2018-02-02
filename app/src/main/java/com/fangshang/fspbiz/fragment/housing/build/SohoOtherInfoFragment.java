package com.fangshang.fspbiz.fragment.housing.build;

import android.os.Bundle;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 11:19
 */

public class SohoOtherInfoFragment extends BaseFragment {
    @BindView(R.id.tv_buildtime)
    TextView mTv_buildtime;
    @BindView(R.id.tv_parknum)
    TextView mTv_parknum;
    @BindView(R.id.tv_parkprice)
    TextView mTv_parkprice;
    @BindView(R.id.tv_floor_height)
    TextView mTv_floor_height;
    @BindView(R.id.tv_floor_num)
    TextView mTv_floor_num;
    @BindView(R.id.tv_elevator)
    TextView mTv_elevator;
    @BindView(R.id.tv_label)
    TextView mTv_label;

    private HttpResponseStruct.BuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sohoother_info;
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
            //建筑年代
            mTv_buildtime.setText(null==buildDetail.approve.buildYear?"无":buildDetail.approve.buildYear+"年");
            //停车位
            mTv_parknum.setText(0==buildDetail.approve.parkAmount ? 0+"个":buildDetail.approve.parkAmount+"个");
            //停车费
            mTv_parkprice.setText(null==buildDetail.approve.parkFee ? "暂无":buildDetail.approve.parkFee+"元/月");
            //层高
            mTv_floor_height.setText(null==buildDetail.approve.floorHeight?"无":buildDetail.approve.floorHeight+"m");
            //层数
            mTv_floor_num.setText(null==buildDetail.approve.floorAmount?"暂无":buildDetail.approve.floorAmount+"层");
            //电梯
            mTv_elevator.setText(null==buildDetail.approve.lift?"暂无信息":buildDetail.approve.lift);
            //楼盘标签
            mTv_label.setText(null==buildDetail.approve.label?"暂无":buildDetail.approve.label);
        }
    }
}
