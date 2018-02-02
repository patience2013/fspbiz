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

public class SohoHouseOtherInfoFragment extends BaseFragment {
    @BindView(R.id.tv_agent_cooperate)
    TextView mTv_agent_cooperate;
    @BindView(R.id.tv_house_purpose)
    TextView mTv_house_purpose;
    @BindView(R.id.tv_provide_rent_free)
    TextView mTv_provide_rent_free;
    @BindView(R.id.tv_decoration)
    TextView mTv_decoration;
    @BindView(R.id.tv_orientation)
    TextView mTv_orientation;
    @BindView(R.id.tv_floor_height)
    TextView mTv_floor_height;
    @BindView(R.id.tv_regist_company)
    TextView mTv_regist_company;
    @BindView(R.id.tv_basic_cfg)
    TextView mTv_basic_cfg;
    @BindView(R.id.tv_extra_cfg)
    TextView mTv_extra_cfg;
    @BindView(R.id.tv_house_description)
    TextView mTv_house_description;

    private HttpResponseStruct.HouseDetail houseDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_house_sohoother_info;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            houseDetail= (HttpResponseStruct.HouseDetail) bundle.getSerializable("houseDetail");
        }
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        if(null!=houseDetail){
            //跟经纪人合作
            mTv_agent_cooperate.setText(0==houseDetail.house.agentCooperate?"否":"是");
            //房源功用
            mTv_house_purpose.setText(null==houseDetail.house.housePurpose ? "无":houseDetail.house.housePurpose);
            //提供免租期
            if(null==houseDetail.house.provideRentFree){
                mTv_provide_rent_free.setText("暂无");
            }else if("1".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("暂无");
            }else if("2".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("一周");
            }else if("3".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("半个月");
            }else if("4".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("二十天");
            }else if("5".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("一个月");
            }else if("6".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("两个月");
            }else if("7".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("三个月");
            }else if("8".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("四个月");
            }else if("9".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("五个月");
            }else if("10".equals(houseDetail.house.provideRentFree)){
                mTv_provide_rent_free.setText("半年");
            }
            //装修情况
            if(null==houseDetail.house.decoration){
                mTv_decoration.setText("暂无");
            }else if("1".equals(houseDetail.house.decoration)){
                mTv_decoration.setText("简装修");
            }else if("2".equals(houseDetail.house.decoration)){
                mTv_decoration.setText("毛坯");
            }else if("3".equals(houseDetail.house.decoration)){
                mTv_decoration.setText("精装修");
            }else if("4".equals(houseDetail.house.decoration)){
                mTv_decoration.setText("豪华装修");
            }
            //朝向
            if(null==houseDetail.house.orientation){
                mTv_orientation.setText("暂无");
            }else if("1".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("东");
            }else if("2".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("南");
            }else if("3".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("西");
            }else if("4".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("北");
            }else if("5".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("东南");
            }else if("6".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("西南");
            }else if("7".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("东北");
            }else if("8".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("西北");
            }else if("9".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("南北");
            }else if("10".equals(houseDetail.house.orientation)){
                mTv_orientation.setText("东西");
            }
            //层高
            mTv_floor_height.setText(null==houseDetail.house.floorHeight?"无":houseDetail.house.floorHeight+"m");
            //能否注册公司
            if(null==houseDetail.house.registCompany){
                mTv_regist_company.setText("暂无信息");
            }else if("1".equals(houseDetail.house.registCompany)){
                mTv_regist_company.setText("是");
            }else if("0".equals(houseDetail.house.registCompany)){
                mTv_regist_company.setText("否");
            }
            //基本配置
            mTv_basic_cfg.setText(null==houseDetail.house.basicCfg?"暂无":houseDetail.house.basicCfg);
            //附加配置
            mTv_extra_cfg.setText(null==houseDetail.house.extraCfg?"暂无信息":houseDetail.house.extraCfg);
            //房源介绍
            mTv_house_description.setText(null==houseDetail.house.houseDescription?"暂无介绍":houseDetail.house.houseDescription);
        }
    }
}
