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

public class PropertyInfoFragment extends BaseFragment {
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

    private HttpResponseStruct.BuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_property_info;
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
            //停车位数量
            mTv_parknum.setText(buildDetail.approve.parkAmount+"个");
            //停车费
            mTv_parkprice.setText(null==buildDetail.approve.parkFee? "暂无信息":buildDetail.approve.parkFee+"/月");
            //物业费
            mTv_property_price.setText(null==buildDetail.approve.propertyFee ?"暂无信息":buildDetail.approve.propertyFee);
            //物业费单位
            mTv_property_util.setText(null==buildDetail.approve.propertyFee? "": "1".equals(buildDetail.approve.propertyUnit)? "元/㎡":"元/月");
            //能耗费
            mTv_energyprice.setText(null ==buildDetail.approve.energyFee ?"暂无信息":buildDetail.approve.energyFee);
            //能耗费单位
            mTv_energy_unit.setText(null==buildDetail.approve.energyFee ? "":"1".equals(buildDetail.approve.energyUnit)?"元/㎡/月":"元/月");
            //物业公司
            mTv_property.setText(null==buildDetail.approve.propertyCompany ? "暂无":buildDetail.approve.propertyCompany);
            //楼盘标签
            mTv_label.setText(null==buildDetail.approve.label?"暂无":buildDetail.approve.label);
        }
    }
}
