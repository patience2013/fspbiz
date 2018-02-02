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

public class PlatformPlantInfoFragment extends BaseFragment {
    @BindView(R.id.tv_build_structure)
    TextView mTv_build_structure;
    @BindView(R.id.tv_factory_area)
    TextView mTv_factory_area;
    @BindView(R.id.tv_floor_height)
    TextView mTv_floor_height;
    @BindView(R.id.tv_floor_bearing)
    TextView mTv_floor_bearing;
    @BindView(R.id.tv_power_capacity)
    TextView mTv_power_capacity;
    @BindView(R.id.tv_car_in_out)
    TextView mTv_car_in_out;
    @BindView(R.id.tv_parknum)
    TextView mTv_parknum;
    @BindView(R.id.tv_goods_lift_num)
    TextView mTv_goods_lift_num;
    @BindView(R.id.tv_min_rent_year)
    TextView mTv_min_rent_year;
    @BindView(R.id.tv_fit_company)
    TextView mTv_fit_company;
    @BindView(R.id.tv_label)
    TextView mTv_label;

    private HttpResponseStruct.PlantformBuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_plant_info;
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
            //建筑结构
            mTv_build_structure.setText(null==buildDetail.estate.buildStructure?"无":buildDetail.estate.buildStructure);
            //厂房面积
            mTv_factory_area.setText(null==buildDetail.estate.factoryBulidingArea ? "无信息":buildDetail.estate.factoryBulidingArea+"㎡");
            //楼层层高
            mTv_floor_height.setText(null==buildDetail.estate.floorHeight?"无":buildDetail.estate.floorHeight+"m");
            //楼层承重量
            mTv_floor_bearing.setText(null==buildDetail.estate.floorBearing?"无":buildDetail.estate.floorHeight+"吨/平");
            //配电容量
            mTv_power_capacity.setText(null==buildDetail.estate.powerCapacity?"无信息":buildDetail.estate.powerCapacity+"KM");
            //车辆出入
            mTv_car_in_out.setText(null==buildDetail.estate.carInOut?"无信息":buildDetail.estate.carInOut);
            //停车位
            mTv_parknum.setText(0==buildDetail.estate.parkAmount?"0个":buildDetail.estate.parkAmount+"个");
            //货梯数
            mTv_goods_lift_num.setText(null==buildDetail.estate.goodsLiftNum?"无信息":buildDetail.estate.goodsLiftNum+"部");
            //最短租期
            mTv_min_rent_year.setText(null==buildDetail.estate.minRentYear?"无信息":buildDetail.estate.minRentYear+"年");
            //适合企业
            mTv_fit_company.setText(null==buildDetail.estate.fitCompany ? "无信息":buildDetail.estate.fitCompany);
            //楼盘标签
            mTv_label.setText(null==buildDetail.estate.label?"暂无":buildDetail.estate.label);
        }
    }
}
