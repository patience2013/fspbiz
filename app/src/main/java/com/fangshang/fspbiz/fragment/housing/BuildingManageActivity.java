package com.fangshang.fspbiz.fragment.housing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.bean.SearchMyBuildEvent;
import com.fangshang.fspbiz.fragment.housing.build.MyReleaseBuildActivity;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.weight.NoSlideViewPager;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 17:25
 */

public class BuildingManageActivity extends BaseActivity {
    @BindView(R.id.slidingTab)
    SegmentTabLayout mSlidingTab;
    @BindView(R.id.vp_housemanage)
    NoSlideViewPager mVp_housemanage;
    @BindView(R.id.tv_city)
    TextView mTv_city;
    @BindView(R.id.lin_city)
    LinearLayout mLin_city;
    @BindView(R.id.et_search)
    EditText mEt_search;

    Gson gson =new Gson();

    private List<Fragment> fraglist = new ArrayList<>();
    private String[] titles = new String[]{"我的楼盘", "平台楼盘"};
    private FragmentManager manager;
    private ArrayList<HttpResponseStruct.Province> proviceItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> cityItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> areaItems = new ArrayList<>();
    private String cityId="";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BuildingManageActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_buildingmanage;
    }
    @Override
    protected void initView() {

        manager =getSupportFragmentManager();
        fraglist.add(new BuildListFragment());//用户楼盘
        fraglist.add(new PlatformBuildFragment());//平台楼盘

//        mSlidingTab.setViewPager(mVp_housemanage,titles, (FragmentActivity) mContext, (ArrayList<Fragment>) fraglist);
        mSlidingTab.setTabData(titles);
        mVp_housemanage.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVp_housemanage.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });


        mEt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                    String keytag = mEt_search.getText().toString().trim();

                    if ("".equals(keytag)) {
                        TsUtils.show("请输入搜索关键字");
                        return false;
                    }
                    SearchMyBuildEvent searchBeanEvent =new SearchMyBuildEvent();
                    searchBeanEvent.cityId=cityId;
                    searchBeanEvent.text=keytag;
                    // 搜索功能主体
                    if(mVp_housemanage.getCurrentItem()==0){
                        EventBus.getDefault().post(searchBeanEvent);
                    }else  if(mVp_housemanage.getCurrentItem()==1){
                        EventBus.getDefault().post(searchBeanEvent);
                    }

                    return true;
                }
                return false;
            }
        });
        getRegion();
    }

    @OnClick({R.id.lin_back,R.id.lin_right,R.id.lin_city})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_right:
                MyReleaseBuildActivity.actionStart(this);
                break;
            case R.id.lin_city:
                ShowPickerView();
                break;
        }
    }
    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = proviceItems.get(options1).getPickerViewText() +
                        cityItems.get(options1).get(options2);
                String text =proviceItems.get(options1).childRegions.get(options2).childRegions.get(options3).name;
                cityId =proviceItems.get(options1).childRegions.get(options2).childRegions.get(options3).id+"";
//                Toast.makeText(CertificationActivity.this,tx,Toast.LENGTH_SHORT).show();
                mTv_city.setText(text);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(proviceItems, cityItems,areaItems);//三级选择器
        pvOptions.show();
    }
    public void getRegion() {
        final long date = new Date().getTime();
        final String number = HttpUtil.getRandom();
        HttpUtil.getSignature(BuildingManageActivity.this, date + "", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.RegionReq regionReq =new HttpRequestStruct.RegionReq();
                HttpRequestStruct.MsgReqWithToken userIdentity = new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(), sign, date + "", number);
                regionReq.msgReq =userIdentity;
                regionReq.isOpen ="1";
                String json = gson.toJson(regionReq);
                OkGo.<BaseBean<HttpResponseStruct.AreaData>>post(InterfaceUrl.GET_REGION)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.AreaData>>(BuildingManageActivity.this) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.AreaData>> response) {
                                Logger.d(date + "number" + number);
                                Logger.d(response.body().getData().regions.get(0).name);
                                List<HttpResponseStruct.Province> provinces = new ArrayList<>();
                                provinces = response.body().getData().regions;
                                proviceItems = (ArrayList<HttpResponseStruct.Province>) response.body().getData().regions;
                                Logger.d(proviceItems.get(0).name);

                                for (int i = 0; i < provinces.size(); i++) {
                                    ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
                                    ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

                                    for (int c = 0; c < provinces.get(i).childRegions.size(); c++) {//遍历该省份的所有城市
                                        String CityName = provinces.get(i).childRegions.get(c).name;
                                        CityList.add(CityName);//添加城市

                                        ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                                        if (provinces.get(i).childRegions.get(c).childRegions == null
                                                || provinces.get(i).childRegions.get(c).childRegions.size() == 0) {
                                            City_AreaList.add("");
                                        } else {

                                            for (int d = 0; d < provinces.get(i).childRegions.get(c).childRegions.size(); d++) {//该城市对应地区所有数据
                                                String AreaName = provinces.get(i).childRegions.get(c).childRegions.get(d).name;

                                                City_AreaList.add(AreaName);//添加该城市所有地区数据
                                            }
                                        }
                                        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                                    }

                                    /**
                                     * 添加城市数据
                                     */
                                    cityItems.add(CityList);

                                    /**
                                     * 添加地区数据
                                     */
                                    areaItems.add(Province_AreaList);
                                }
                            }
                        });

            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fraglist.size();
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
}
