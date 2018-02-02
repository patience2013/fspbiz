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

public class PlatformPropertyInfoFragment extends BaseFragment {
    @BindView(R.id.tv_parknum)
    TextView mTv_parknum;
    @BindView(R.id.tv_parkprice)
    TextView mTv_parkprice;
    @BindView(R.id.tv_property_price)
    TextView mTv_property_price;
    @BindView(R.id.tv_property_util)
    TextView mTv_property_util;
    @BindView(R.id.tv_energyprice)
    TextView mTv_energyprice;
    @BindView(R.id.tv_energy_unit)
    TextView mTv_energy_unit;
    @BindView(R.id.tv_property)
    TextView mTv_property;
    @BindView(R.id.tv_label)
    TextView mTv_label;

    private HttpResponseStruct.PlantformBuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_property_info;
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
            //停车位数量
            mTv_parknum.setText(buildDetail.estate.parkAmount+"个");
            //停车费
            mTv_parkprice.setText(null==buildDetail.estate.parkFee? "暂无信息":buildDetail.estate.parkFee+"/月");
            //物业费
            mTv_property_price.setText(null==buildDetail.estate.propertyFee ?"暂无信息":buildDetail.estate.propertyFee);
            //物业费单位
            mTv_property_util.setText(null==buildDetail.estate.propertyFee? "": "1".equals(buildDetail.estate.propertyUnit)? "元/㎡":"元/月");
            //能耗费
            mTv_energyprice.setText(null ==buildDetail.estate.energyFee ?"暂无信息":buildDetail.estate.energyFee);
            //能耗费单位
            mTv_energy_unit.setText(null==buildDetail.estate.energyFee ? "":"1".equals(buildDetail.estate.energyUnit)?"元/㎡/月":"元/月");
            //物业公司
            mTv_property.setText(null==buildDetail.estate.propertyCompany ? "暂无":buildDetail.estate.propertyCompany);
            //楼盘标签
            mTv_label.setText(null==buildDetail.estate.label?"暂无":buildDetail.estate.label);
        }
    }
}
