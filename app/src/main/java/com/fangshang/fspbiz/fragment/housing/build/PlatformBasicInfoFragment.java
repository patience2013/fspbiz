package com.fangshang.fspbiz.fragment.housing.build;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 11:19
 */

public class PlatformBasicInfoFragment extends BaseFragment {
    @BindView(R.id.tv_buildtype)
    TextView  mTv_buildtype;
    @BindView(R.id.tv_type)
    TextView  mTv_type;
    @BindView(R.id.tv_buildarea)
    TextView  mTv_buildarea;
    @BindView(R.id.tv_contact_people)
    TextView  mTv_contact_people;
    @BindView(R.id.tv_contact_mobile)
    TextView  mTv_contact_mobile;
    @BindView(R.id.tv_contact_tel)
    TextView  mTv_contact_tel;
    @BindView(R.id.tv_area)
    TextView  mTv_area;
    @BindView(R.id.tv_businessdistrict)
    TextView  mTv_businessdistrict;
    @BindView(R.id.tv_address)
    TextView  mTv_address;
    @BindView(R.id.tv_introduction)
    TextView  mTv_introduction;
    @BindView(R.id.map)
    MapView mMapView;
    AMap aMap;

    private HttpResponseStruct.PlantformBuildDetail buildDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_basicinfo;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            try {
                buildDetail= (HttpResponseStruct.PlantformBuildDetail) bundle.getSerializable("buildDetail");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(null);
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        LatLng latLng = new LatLng(30.335111,120.111026);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("所在地").snippet("DefaultMarker"));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        if(null!=buildDetail){
            //楼盘类型
            if(buildDetail.estate.estateType==1){
                mTv_buildtype.setText("写字楼");
            }else if(buildDetail.estate.estateType==2){
                mTv_buildtype.setText("联合办公");
            }else if(buildDetail.estate.estateType==3){
                mTv_buildtype.setText("酒店式soho");
            }else if(buildDetail.estate.estateType==4){
                mTv_buildtype.setText("厂房");
            }
            //租售类型
            if(buildDetail.estate.rentSaleType==1){
                mTv_type.setText("租");
            }else if(buildDetail.estate.rentSaleType==2){
                mTv_type.setText("售");
            }
            //建筑面积
            mTv_buildarea.setText(null==buildDetail.estate.constructionArea? "暂无":buildDetail.estate.constructionArea+"㎡");
            //联系人
            mTv_contact_people.setText(null==buildDetail.estate.contact? "无":buildDetail.estate.contact);
            //联系人手机
            mTv_contact_mobile.setText(null==buildDetail.estate.contactCellphone? "无":buildDetail.estate.contactCellphone);
            //联系人座机
            mTv_contact_tel.setText(null==buildDetail.estate.contactTel? "无":buildDetail.estate.contactTel);
            //所在市区
            mTv_area.setText(null==buildDetail.estate.zoneName?"无":buildDetail.estate.zoneName);
//            mTv_area.setText(135465465==buildDetail.approve.prov ?"无":buildDetail.approve.prov+buildDetail.approve.city);
            //所在详细地址
            mTv_address.setText(null==buildDetail.estate.estateAddress?"无":buildDetail.estate.estateAddress);
            //商圈
            mTv_businessdistrict.setText(null==buildDetail.estate.businessCircleName? "无":buildDetail.estate.businessCircleName);
            //楼盘介绍
            mTv_introduction.setText(null ==buildDetail.estate.estateDescription ? "暂无楼盘描叙":buildDetail.estate.estateDescription);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
}
