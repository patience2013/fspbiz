package com.fangshang.fspbiz.fragment.housing.build;

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
import com.fangshang.fspbiz.fragment.housing.AddBuildActivity;
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

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/23/023 9:50
 */

public class BuildDetailActivity extends BaseBackActivity {
    @BindView(R.id.vp_head)
    NoSlideViewPager mVp_head;
    @BindView(R.id.radio_group)
    RadioGroup mRadio_group;
    @BindView(R.id.slidingTab)
    SegmentTabLayout mSlidingTab;
    @BindView(R.id.vp_detail)
    DetailNoSlideViewPager mVp_detail;
    @BindView(R.id.tv_name)
    TextView mTv_name;
    @BindView(R.id.tv_area)
    TextView mTv_area;
    @BindView(R.id.tv_price)
    TextView mTv_price;
    @BindView(R.id.tv_util)
    TextView mTv_util;
    @BindView(R.id.tv_time)
    TextView mTv_time;

    private Gson gson =new Gson();
    private String[] titles ={"图片","视频"};
    private List<Fragment> fraglist = new ArrayList<>();
    private FragmentManager manager;

    private List<Fragment> fraglistDetail = new ArrayList<>();
    private String[] titlesDetail = new String[]{"基本信息", "物业信息","审核情况"};
    private int estateId=1;//楼盘id
    private int buildtype=2; //1\我发布的2其他类型
    HttpResponseStruct.PlantformBuildDetail PlantformBuildDetail;
    private List<HttpResponseStruct.Attachment> attachments =new ArrayList<>();
    private List<HttpResponseStruct.Attachment> images =new ArrayList<>();
    private List<HttpResponseStruct.Attachment> videos =new ArrayList<>();

    public static void actionStart(Context context,int estateId,int buildtype) {
        Intent intent = new Intent(context, BuildDetailActivity.class);
        intent.putExtra("estateId",estateId);
        intent.putExtra("buildtype",buildtype);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_builddetail;
    }

    @Override
    protected void initView() {
        estateId =getIntent().getIntExtra("estateId",0);
        buildtype =getIntent().getIntExtra("buildtype",0);
        manager =getSupportFragmentManager();



        getData(estateId);
    }

    @OnClick({R.id.img_back,R.id.tv_update})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_update:
                AddBuildActivity.actionStart(this,PlantformBuildDetail.estate.id+"");
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

    public void getData(final int estateId){
        final long date = new Date().getTime();
        final String number= HttpUtil.getRandom();
        HttpUtil.getSignature(BuildDetailActivity.this, date+"", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.PlatformBuildDetailReq myBuildDetailReq =new HttpRequestStruct.PlatformBuildDetailReq();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                myBuildDetailReq.msgReq =msgReqWithToken;
                myBuildDetailReq.estateId =estateId;
                String json = gson.toJson(myBuildDetailReq);
                OkGo.<BaseBean<HttpResponseStruct.PlantformBuildData>>post(InterfaceUrl.MY_PLATFORM_BUILD_DETAIL)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.PlantformBuildData>>(BuildDetailActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.PlantformBuildData>> response) {
                                Logger.d(response.body().getResultMsg()+"code"+response.body().getResultCode()+""+response.body().getData().detail);
                                if("00000".equals(response.body().getResultCode())){
                                    PlantformBuildDetail=response.body().getData().detail;
                                    if(null!=PlantformBuildDetail){
                                        attachments = PlantformBuildDetail.attachments;
                                        for(HttpResponseStruct.Attachment attachment:attachments){
                                            if(attachment.fileType==1){
                                                images.add(attachment);
                                            }else if(attachment.fileType==2){
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
                                        PlatformBasicInfoFragment basicInfoFragment =new PlatformBasicInfoFragment();
                                        Bundle bundle =new Bundle();
                                        bundle.putSerializable("buildDetail",PlantformBuildDetail);
                                        basicInfoFragment.setArguments(bundle);
                                        fraglistDetail.add(basicInfoFragment);
                                        //物业信息
                                        PlatformPropertyInfoFragment propertyInfoFragment =new PlatformPropertyInfoFragment();
                                        propertyInfoFragment.setArguments(bundle);

                                        //联合办公 空间信息
                                        PlatformSpaceInfoFragment spaceInfoFragment =new PlatformSpaceInfoFragment();
                                        spaceInfoFragment.setArguments(bundle);

                                        //酒店式SOHO 其他信息
                                        PlatformSohoOtherInfoFragment sohoOtherInfoFragment =new PlatformSohoOtherInfoFragment();
                                        sohoOtherInfoFragment.setArguments(bundle);

                                        //厂房信息
                                        PlatformPlantInfoFragment plantInfoFragment=new PlatformPlantInfoFragment();
                                        plantInfoFragment.setArguments(bundle);

                                        //审核信息
                                        PlatformAuditInfoFragment auditInfoFragment =new PlatformAuditInfoFragment();
                                        auditInfoFragment.setArguments(bundle);
                                        if(PlantformBuildDetail.estate.estateType==1 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "物业信息"};
                                            fraglistDetail.add(propertyInfoFragment);
                                        }if(PlantformBuildDetail.estate.estateType==1 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "物业信息","审核情况"};
                                            fraglistDetail.add(propertyInfoFragment);
                                            fraglistDetail.add(auditInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==2 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "空间信息"};
                                            fraglistDetail.add(spaceInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==2 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "空间信息","审核情况"};
                                            fraglistDetail.add(spaceInfoFragment);
                                            fraglistDetail.add(auditInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==3 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "其他信息"};
                                            fraglistDetail.add(sohoOtherInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==3 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "其他信息","审核情况"};
                                            fraglistDetail.add(sohoOtherInfoFragment);
                                            fraglistDetail.add(auditInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==4 && buildtype!=1){
                                            titlesDetail =new String[]{"基本信息", "厂房信息"};
                                            fraglistDetail.add(plantInfoFragment);
                                        }else if(PlantformBuildDetail.estate.estateType==4 && buildtype==1){
                                            titlesDetail =new String[]{"基本信息", "厂房信息","审核情况"};
                                            fraglistDetail.add(plantInfoFragment);
                                            fraglistDetail.add(auditInfoFragment);
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


                                        mTv_name.setText(PlantformBuildDetail.estate.estateName);//楼盘名称
                                        mTv_area.setText(PlantformBuildDetail.estate.startArea+"-"+PlantformBuildDetail.estate.endArea+"㎡");
                                        mTv_price.setText("￥"+PlantformBuildDetail.estate.startPrice);
                                        if(PlantformBuildDetail.estate.unit==1){
                                            mTv_util.setText("元/m²/天起");
                                        }else if(PlantformBuildDetail.estate.unit==2){
                                            mTv_util.setText("元/m²/月起");
                                        }else if(PlantformBuildDetail.estate.unit==3){
                                            mTv_util.setText("元/月起");
                                        }else if(PlantformBuildDetail.estate.unit==4){
                                            mTv_util.setText("元/m²/天");
                                        }else if(PlantformBuildDetail.estate.unit==5){
                                            mTv_util.setText("元/m²/月");
                                        }else if(PlantformBuildDetail.estate.unit==6){
                                            mTv_util.setText("元/月");
                                        }else if(PlantformBuildDetail.estate.unit==7){
                                            mTv_util.setText("元/m²");
                                        }else if(PlantformBuildDetail.estate.unit==8){
                                            mTv_util.setText("元");
                                        }else if(PlantformBuildDetail.estate.unit==9){
                                            mTv_util.setText("元/工位/月");
                                        }else if(PlantformBuildDetail.estate.unit==10){
                                            mTv_util.setText("元/间/月");
                                        }
                                        mTv_time.setText(ZhuanHuanUtil.getTimes(PlantformBuildDetail.estate.updateTime+""));

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
