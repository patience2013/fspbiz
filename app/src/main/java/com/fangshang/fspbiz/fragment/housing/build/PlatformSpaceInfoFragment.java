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

public class PlatformSpaceInfoFragment extends BaseFragment {
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

    private HttpResponseStruct.PlantformBuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_space_info;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            buildDetail= (HttpResponseStruct.PlantformBuildDetail) bundle.getSerializable("buildDetail");
        }
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        if(null!=buildDetail){
            //出租方式
            mTv_rent_way.setText(null==buildDetail.estate.rentType?"无":buildDetail.estate.rentType);
            //所在楼层
            mTv_floor_num.setText(null==buildDetail.estate.floor? "无":buildDetail.estate.floor+"层");
            //空间面积
            mTv_space_area.setText(null==buildDetail.estate.sapceArea ?"无":buildDetail.estate.sapceArea);
            //总工位
            mTv_station_num.setText(null==buildDetail.estate.workStation? "": buildDetail.estate.workStation+"个");
            //入驻要求
            mTv_entry_conditions.setText(null ==buildDetail.estate.parkRequire ?"暂无":buildDetail.estate.parkRequire);
            //停车位
            mTv_parknum.setText(0==buildDetail.estate.parkAmount ? 0+"个":buildDetail.estate.parkAmount+"个");
            //停车费
            mTv_parkprice.setText(null==buildDetail.estate.parkFee ? "暂无":buildDetail.estate.parkFee+"元/月");
            //服务平台
            mTv_service_platform.setText(null==buildDetail.estate.servicePlatform?"暂无":buildDetail.estate.servicePlatform);
            //服务空间
            mTv_service_space.setText(null==buildDetail.estate.spaceService?"暂无":buildDetail.estate.spaceService);
            //楼盘标签
            mTv_label.setText(null==buildDetail.estate.label?"暂无":buildDetail.estate.label);
        }
    }
}
