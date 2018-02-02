package com.fangshang.fspbiz.fragment.housing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.DetailHead;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.fragment.housing.build.AuditHouseInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.AuditInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.BasicHouseInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.BasicInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.ImageFragment;
import com.fangshang.fspbiz.fragment.housing.build.OfficeHouseOtherInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.PlantInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.PropertyInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.SohoHouseOtherInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.SohoOtherInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.SpaceInfoFragment;
import com.fangshang.fspbiz.fragment.housing.build.UserBuildDetailActivity;
import com.fangshang.fspbiz.fragment.housing.build.VideolistFragment;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;
import com.fangshang.fspbiz.weight.DetailNoSlideViewPager;
import com.fangshang.fspbiz.weight.NoSlideViewPager;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/23/023 9:50
 */

public class HouseDetailActivity extends BaseBackActivity {
    @BindView(R.id.vp_head)
    NoSlideViewPager mVp_head;
    @BindView(R.id.radio_group)
    RadioGroup mRadio_group;
    @BindView(R.id.slidingTab)
    SegmentTabLayout mSlidingTab;
    @BindView(R.id.vp_detail)
    DetailNoSlideViewPager mVp_detail;

    @BindView(R.id.tv_housenum)
    TextView mTv_housenum;
    @BindView(R.id.tv_area)
    TextView mTv_area;
    @BindView(R.id.tv_price)
    TextView mTv_price;
    @BindView(R.id.tv_util)
    TextView mTv_util;
    @BindView(R.id.tv_time)
    TextView mTv_time;
    @BindView(R.id.tv_push_num)
    TextView mTv_push_num;
    @BindView(R.id.tv_status)
    TextView mTv_status;

    private Gson gson =new Gson();
    private String[] titles ={"图片","视频"};
    private List<Fragment> fraglist = new ArrayList<>();
    private FragmentManager manager;

    private List<Fragment> fraglistDetail = new ArrayList<>();
    private String[] titlesDetail = new String[]{"基本信息", "物业信息","审核情况"};
    private int houseId=1;
    private int buildtype=2; //1\我发布的2其他类型
    private List<HttpResponseStruct.Attachment> attachments =new ArrayList<>();
    private List<HttpResponseStruct.Attachment> images =new ArrayList<>();
    private List<HttpResponseStruct.Attachment> videos =new ArrayList<>();
    private int buildId;
    private String buildName;
    HttpResponseStruct.HouseDetail houseDetail;
    public static void actionStart(Context context,int houseId,int buildtype) {
        Intent intent = new Intent(context, HouseDetailActivity.class);
        intent.putExtra("houseId",houseId);
        intent.putExtra("buildtype",buildtype);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_housedetail;
    }

    @Override
    protected void initView() {
        houseId =getIntent().getIntExtra("houseId",0);
        buildtype =getIntent().getIntExtra("buildtype",0);
        manager =getSupportFragmentManager();



        getData(houseId);
    }

    @OnClick({R.id.img_back,R.id.tv_update})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_update:
                AddHouseActivity.actionStart(HouseDetailActivity.this,buildId+"",buildName+"",houseId+"");
                break;
        }
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fraglist.get(position);
        }
    }
    private class MyPagerDetailAdapter extends FragmentPagerAdapter {
        public MyPagerDetailAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titlesDetail.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesDetail[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fraglistDetail.get(position);
        }
    }

    public void getData(final int houseId){
        final long date = new Date().getTime();
        final String number= HttpUtil.getRandom();
        HttpUtil.getSignature(HouseDetailActivity.this, date+"", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.MyHouseDetailReq myBuildDetailReq =new HttpRequestStruct.MyHouseDetailReq();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                myBuildDetailReq.msgReq =msgReqWithToken;
                myBuildDetailReq.houseId =houseId;
                String json = gson.toJson(myBuildDetailReq);
                OkGo.<BaseBean<HttpResponseStruct.HouseData>>post(InterfaceUrl.MY_HOUSE_DETAIL)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.HouseData>>(HouseDetailActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.HouseData>> response) {
                                Logger.d(response.body().getResultMsg()+"code"+response.body().getResultCode());
                                if("00000".equals(response.body().getResultCode())){
                                    houseDetail=response.body().getData().detail;
                                    if(null!=houseDetail){
                                        buildId =houseDetail.house.estateId;
                                        buildName =houseDetail.house.estateName;

                                        attachments = houseDetail.attachments;
                                        for(HttpResponseStruct.Attachment attachment:attachments){
                                            if(attachment.fileType==1){
                                                images.add(attachment);
                                            }else if(attachment.fileType==0){
                                                videos.add(attachment);
                                            }
                                        }
                                        ImageFragment imageFragment= new ImageFragment();
                                        Bundle bundleImage =new Bundle();
                                        DetailHead imagehead =new DetailHead();
                                        imagehead.attachments =images;
                                        bundleImage.putSerializable("imagehead", imagehead);
                                        imageFragment.setArguments(bundleImage);
                                        fraglist.add(imageFragment);

                                        VideolistFragment videolistFragment= new VideolistFragment();
                                        Bundle bundleVideo =new Bundle();
                                        DetailHead videohead =new DetailHead();
                                        videohead.attachments =videos;
                                        bundleVideo.putSerializable("videohead", videohead);
                                        videolistFragment.setArguments(bundleVideo);
                                        fraglist.add(videolistFragment);

                                        mVp_head.setAdapter(new MyPagerAdapter(manager));
                                        mVp_head.setCurrentItem(0);
                                        mRadio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                switch (checkedId){
                                                    case R.id.rbt_image:
                                                        mVp_head.setCurrentItem(0);
                                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(40));
                                                        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                                                        lp.setMargins(0, 0, 0, 0);
                                                        mRadio_group.setLayoutParams(lp);
                                                        break;
                                                    case R.id.rbt_video:
                                                        mVp_head.setCurrentItem(1);
                                                        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(40));
                                                        lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                                                        lp1.setMargins(0, 0, 0, 80);
                                                        mRadio_group.setLayoutParams(lp1);
                                                        break;
                                                }
                                            }
                                        });
                                        //基本信息
                                        BasicHouseInfoFragment basicInfoFragment =new BasicHouseInfoFragment();
                                        Bundle bundle =new Bundle();
                                        bundle.putSerializable("houseDetail",houseDetail);
                                        basicInfoFragment.setArguments(bundle);
                                        fraglistDetail.add(basicInfoFragment);
                                        //SOHO其他信息
                                        SohoHouseOtherInfoFragment sohoHouseOtherInfoFragment =new SohoHouseOtherInfoFragment();
                                        sohoHouseOtherInfoFragment.setArguments(bundle);

                                        //写字楼其他信息
                                        OfficeHouseOtherInfoFragment officeHouseOtherInfoFragment =new OfficeHouseOtherInfoFragment();
                                        officeHouseOtherInfoFragment.setArguments(bundle);


                                        //审核信息
                                        AuditHouseInfoFragment auditHouseInfoFragment =new AuditHouseInfoFragment();
                                        auditHouseInfoFragment.setArguments(bundle);
                                        if(houseDetail.house.houseType==1 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "其他信息"};
                                            fraglistDetail.add(officeHouseOtherInfoFragment);
                                        }if(houseDetail.house.houseType==1 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "其他信息","审核情况"};
                                            fraglistDetail.add(officeHouseOtherInfoFragment);
                                            fraglistDetail.add(auditHouseInfoFragment);
                                        }else if(houseDetail.house.houseType==2 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "其他信息"};
                                            fraglistDetail.add(sohoHouseOtherInfoFragment);
                                        }else if(houseDetail.house.houseType==2 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "其他信息","审核情况"};
                                            fraglistDetail.add(sohoHouseOtherInfoFragment);
                                            fraglistDetail.add(auditHouseInfoFragment);
                                        }

                                        mSlidingTab.setTabData(titlesDetail);
                                        mVp_detail.setAdapter(new MyPagerDetailAdapter(getSupportFragmentManager()));
                                        mSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
                                            @Override
                                            public void onTabSelect(int position) {
                                                mVp_detail.setCurrentItem(position);
                                            }

                                            @Override
                                            public void onTabReselect(int position) {
                                            }
                                        });

//                                        if(!"".equals(houseDetail.house.building)){
//
//                                        }
                                        mTv_housenum.setText(houseDetail.house.building+"幢"+houseDetail.house.floor+"层"+houseDetail.house.houeNo+"室");//所属房间号
                                        if(null==houseDetail.house.refreshTime){
                                            mTv_time.setText("无");
                                        }else{
                                            mTv_time.setText(ZhuanHuanUtil.getTimes(houseDetail.house.refreshTime+""));
                                        }

                                        mTv_price.setText("￥"+houseDetail.house.price);
                                        //单位 1:元/㎡/天 2:元/㎡/月 3:元/月  4:元/㎡ 5:元
                                        if(houseDetail.house.unit==1){
                                            mTv_util.setText("元/m²/天");
                                        }else if(houseDetail.house.unit==2){
                                            mTv_util.setText("元/m²/月");
                                        }else if(houseDetail.house.unit==3){
                                            mTv_util.setText("元/月");
                                        }else if(houseDetail.house.unit==4){
                                            mTv_util.setText("元/m²");
                                        }else if(houseDetail.house.unit==5){
                                            mTv_util.setText("元");
                                        }
                                        mTv_area.setText(houseDetail.house.area+"㎡");//面积
                                        mTv_push_num.setText(houseDetail.house.pushAmount+"次");
                                        mTv_status.setText(houseDetail.house.isOnSale==1?"上架":"未上架");

                                    }
                                }else{
                                    TsUtils.show(response.body().getResultMsg());
                                }

                            }
                        });
            }
        });
    }
}
