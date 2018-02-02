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

public class BasicHouseInfoFragment extends BaseFragment {
    @BindView(R.id.tv_belong_build)
    TextView  mTv_belong_build;
    @BindView(R.id.tv_housetype)
    TextView  mTv_housetype;
    @BindView(R.id.tv_rent_type)
    TextView  mTv_rent_type;
    @BindView(R.id.tv_pay_type)
    TextView  mTv_pay_type;
    @BindView(R.id.tv_contact)
    TextView  mTv_contact;
    @BindView(R.id.tv_contact_tel)
    TextView  mTv_contact_tel;

    private HttpResponseStruct.HouseDetail houseDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_basichouseinfo;
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
            //所属楼盘
            mTv_belong_build.setText(null==houseDetail.house.estateName? "无":houseDetail.house.estateName);
            //房源类型
            if(houseDetail.house.houseType==1){
                mTv_housetype.setText("写字楼");
            }else if(houseDetail.house.houseType==2){
                mTv_housetype.setText("酒店式soho");
            }else if(houseDetail.house.houseType==3){
                mTv_housetype.setText("酒店式soho");
            }else if(houseDetail.house.houseType==4){
                mTv_housetype.setText("厂房");
            }
            //租售类型
            if(houseDetail.house.rentType==1){
                mTv_rent_type.setText("租");
            }else if(houseDetail.house.rentType==2){
                mTv_rent_type.setText("售");
            }
            //付款方式
            mTv_pay_type.setText("押"+houseDetail.house.depositMonth+"付"+houseDetail.house.payMonth);
            //联系人
            mTv_contact.setText(null==houseDetail.house.contactPerson? "无":houseDetail.house.contactPerson);
            //联系方式
            mTv_contact_tel.setText(null==houseDetail.house.contactCellphone? "无":houseDetail.house.contactCellphone);
        }
    }

}
