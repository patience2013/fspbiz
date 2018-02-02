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

public class SpaceInfoFragment extends BaseFragment {
    @BindView(R.id.tv_rent_way)
    TextView mTv_rent_way;
    @BindView(R.id.tv_floor_num)
    TextView mTv_floor_num;
    @BindView(R.id.tv_space_area)
    TextView mTv_space_area;
    @BindView(R.id.tv_station_num)
    TextView mTv_station_num;
    @BindView(R.id.tv_entry_conditions)
    TextView mTv_entry_conditions;
    @BindView(R.id.tv_parknum)
    TextView mTv_parknum;
    @BindView(R.id.tv_parkprice)
    TextView mTv_parkprice;
    @BindView(R.id.tv_service_platform)
    TextView mTv_service_platform;
    @BindView(R.id.tv_service_space)
    TextView mTv_service_space;
    @BindView(R.id.tv_label)
    TextView mTv_label;

    private HttpResponseStruct.BuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_space_info;
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
            //出租方式
            mTv_rent_way.setText(null==buildDetail.approve.rentType?"无":buildDetail.approve.rentType);
            //所在楼层
            mTv_floor_num.setText(null==buildDetail.approve.floor? "无":buildDetail.approve.floor+"层");
            //空间面积
            mTv_space_area.setText(null==buildDetail.approve.sapceArea ?"无":buildDetail.approve.sapceArea);
            //总工位
            mTv_station_num.setText(null==buildDetail.approve.workStation? "": buildDetail.approve.workStation+"个");
            //入驻要求
            mTv_entry_conditions.setText(null ==buildDetail.approve.parkRequire ?"暂无":buildDetail.approve.parkRequire);
            //停车位
            mTv_parknum.setText(0==buildDetail.approve.parkAmount ? 0+"个":buildDetail.approve.parkAmount+"个");
            //停车费
            mTv_parkprice.setText(null==buildDetail.approve.parkFee ? "暂无":buildDetail.approve.parkFee+"元/月");
            //服务平台
            mTv_service_platform.setText(null==buildDetail.approve.servicePlatform?"暂无":buildDetail.approve.servicePlatform);
            //服务空间
            mTv_service_space.setText(null==buildDetail.approve.spaceService?"暂无":buildDetail.approve.spaceService);
            //楼盘标签
            mTv_label.setText(null==buildDetail.approve.label?"暂无":buildDetail.approve.label);
        }
    }
}
