package com.fangshang.fspbiz.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.login.CertificationActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.bean.MyHouseListEvent;
import com.fangshang.fspbiz.bean.RentBean;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.interfaceUrl.JsonCallBack;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.SelectAreaDialog;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.weight.listener.OnClickListenerWrapper;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/8/008 20:57
 */

public class FilterView extends RelativeLayout {
    public String cityId;//区域id
    public int prov;//省id
    public int city;//市id
    public int district;//区id
    public String estateId; //楼盘id
    public String houseType;//房源类型 1:写字楼 2:酒店式soho
    public String rentType	;//租售类型 1:租 2:售
    public String startPrice;//开始价格
    public String endPrice;//结束价格
    public String priceUnit;//单位 1:元/㎡/天 2:元/㎡/月 3:元/月 4:元/㎡ 5:元
    public String startArea	;//	开始面积
    public String endArea;//结束面积
    public String decoration;//装修情况 1:简装修 2:毛培 3:精装修 4：豪华装修
    public String provideRentFree;//提供免租期 1：无 2: 1周 3：半个月 4：20天 5:1个月 6:2个月 7:3个月 8:4个月 9:5个月 10:6个月
    private Gson gson =new Gson();
    private Context mContext;
    private Button mBtn_reset,mBtn_ensure;
    private LinearLayout mLin_address_content;
    private LinearLayout mLin_address,mLin_building,mLin_buildingtypetitle,mLin_houseprice,mLin_housearea,mLin_type,mLin_happening,mLin_mianzu;
    private LinearLayout mLin_buildingType,mLin_priceRange,mLin_arearange,mLin_type_select,mLin_decoration,mLin_mianzudate;
    private TextView mTv_area;
    private CheckBox mCb_all,mCb_xiezilou,mCb_soho;//房源类型
    private CheckBox mCb_rent,mCb_sell; //租售类型
    private RecyclerView mRv_list,mRv_buildlist,mRv_decorationlist;
    private EditText mEt_startprice,mEt_endprice,mEt_startarea,mEt_endarea;
    private TextView mTv_unit;



    private ArrayList<HttpResponseStruct.Province> proviceItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> cityItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> areaItems = new ArrayList<>();
    private List<HttpResponseStruct.Area> areas = new ArrayList<>();
    
    private List<RentBean> rentBeans =new ArrayList<>();
    private List<RentBean> decorations =new ArrayList<>();
    private DecorationAdapter decorationAdapter;
    private RentsAdapter rentAdapter;
    private BuildsAdapter buildsAdapter;
    private int pageNum=1;
    private int pageCount;
    private List<HttpResponseStruct.BuildSimpleDetail> listDatas =new ArrayList<>();
    private String[] rents =new String[]{"全部","暂无","一周","半个月","二十天","一个月","二个月","三个月","四个月","五个月","半年"};
    private String[] units =new String[]{"元/㎡/天 ","元/㎡/月","元/月","元/㎡","元"};

    //单位选择
    private OptionsPickerView pvCustomOptions;
    public FilterView(Context context) {
        super(context);
        mContext =context;
        initCustomOptionPicker();
        inflateView();
        getRegion();
        getData(1,"","");
        initData();
    }
    private void initData(){
        RentBean rentBean =new RentBean("全部",false);
        RentBean rentBean1 =new RentBean("暂无",false);
        RentBean rentBean2 =new RentBean("一周",false);
        RentBean rentBean3 =new RentBean("半个月",false);
        RentBean rentBean4 =new RentBean("二十天",false);
        RentBean rentBean5 =new RentBean("一个月",false);
        RentBean rentBean6 =new RentBean("二个月",false);
        RentBean rentBean7 =new RentBean("三个月",false);
        RentBean rentBean8 =new RentBean("四个月",false);
        RentBean rentBean9 =new RentBean("五个月",false);
        RentBean rentBean0 =new RentBean("半年",false);
        rentBeans.add(rentBean);
        rentBeans.add(rentBean1);
        rentBeans.add(rentBean2);
        rentBeans.add(rentBean3);
        rentBeans.add(rentBean4);
        rentBeans.add(rentBean5);
        rentBeans.add(rentBean6);
        rentBeans.add(rentBean7);
        rentBeans.add(rentBean8);
        rentBeans.add(rentBean9);
        rentBeans.add(rentBean0);


        RentBean all =new RentBean("全部",false);
        RentBean simple =new RentBean("简装修",false);
        RentBean good =new RentBean("精装修",false);
        RentBean best =new RentBean("豪华装修",false);
        RentBean bed =new RentBean("毛坯",false);
        decorations.add(all);
        decorations.add(simple);
        decorations.add(good);
        decorations.add(best);
        decorations.add(bed);

        decorationAdapter =new DecorationAdapter(decorations);
        mRv_decorationlist.setAdapter(decorationAdapter);
        rentAdapter =new RentsAdapter(rentBeans);
        mRv_list.setAdapter(rentAdapter);
        buildsAdapter =new BuildsAdapter(listDatas);
        mRv_buildlist.setAdapter(buildsAdapter);
    }
    private void inflateView() {
        View view =View.inflate(getContext(), R.layout.filter_layout, this);
        //房源类型
        mCb_all =view.findViewById(R.id.cb_all);
        mCb_xiezilou =view.findViewById(R.id.cb_xiezilou);
        mCb_soho =view.findViewById(R.id.cb_soho);
        //租售类型
        mCb_rent =view.findViewById(R.id.cb_rent);
        mCb_sell =view.findViewById(R.id.cb_sell);
        //楼盘列表和免租期列表
        mRv_list =view.findViewById(R.id.rv_list);
        mRv_buildlist =view.findViewById(R.id.rv_buildlist);
        mRv_decorationlist =view.findViewById(R.id.rv_decorationlist);
        //开始价格和结束价格
        mEt_startprice =view.findViewById(R.id.et_startprice);
        mEt_endprice =view.findViewById(R.id.et_endprice);
        //单位
        mTv_unit =view.findViewById(R.id.tv_unit);
        //房源面积
        mEt_startarea =view.findViewById(R.id.et_startarea);
        mEt_endarea =view.findViewById(R.id.et_endarea);

        mLin_address_content =view.findViewById(R.id.lin_address_content);
        mBtn_reset =view.findViewById(R.id.btn_reset);
        mBtn_ensure =view.findViewById(R.id.btn_ok);
        mLin_address =view.findViewById(R.id.lin_address);
        mLin_building =view.findViewById(R.id.lin_building);
        mLin_buildingtypetitle =view.findViewById(R.id.lin_buildingtypetitle);
        mLin_houseprice =view.findViewById(R.id.lin_houseprice);
        mLin_housearea =view.findViewById(R.id.lin_housearea);
        mLin_type =view.findViewById(R.id.lin_type);
        mLin_happening =view.findViewById(R.id.lin_happening);
        mLin_mianzu =view.findViewById(R.id.lin_mianzu);
        mTv_area =view.findViewById(R.id.tv_area);

        mLin_buildingType =view.findViewById(R.id.lin_buildingType);
        mLin_priceRange =view.findViewById(R.id.lin_priceRange);
        mLin_arearange =view.findViewById(R.id.lin_arearange);
        mLin_type_select =view.findViewById(R.id.lin_type_select);
        mLin_decoration =view.findViewById(R.id.lin_decoration);
        mLin_mianzudate =view.findViewById(R.id.lin_mianzudate);

        mLin_address_content.setOnClickListener(mOnClickListener);
        mBtn_reset.setOnClickListener(mOnClickListener);
        mBtn_ensure.setOnClickListener(mOnClickListener);
        mLin_address.setOnClickListener(mOnClickListener);
        mLin_building.setOnClickListener(mOnClickListener);
        mLin_buildingtypetitle.setOnClickListener(mOnClickListener);
        mLin_houseprice.setOnClickListener(mOnClickListener);
        mLin_housearea.setOnClickListener(mOnClickListener);
        mLin_type.setOnClickListener(mOnClickListener);
        mLin_happening.setOnClickListener(mOnClickListener);
        mLin_mianzu.setOnClickListener(mOnClickListener);
        mTv_unit.setOnClickListener(mOnClickListener);

//        selectList = (ListView) findViewById(R.id.selsectFrameLV);
//        backBrand = (ImageView) findViewById(R.id.select_brand_back_im);
//        resetBrand = (Button) findViewById(R.id.fram_reset_but);
//        mRelateLay = (RelativeLayout) findViewById(R.id.select_frame_lay);
//        okBrand = (Button) findViewById(R.id.fram_ok_but);
//        resetBrand.setOnClickListener(mOnClickListener);
//        okBrand.setOnClickListener(mOnClickListener);
//        backBrand.setOnClickListener(mOnClickListener);
//        mRelateLay.setOnClickListener(mOnClickListener);
//        setUpList();
        //房源类型
        mCb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    houseType ="";
                    mCb_xiezilou.setChecked(false);
                    mCb_soho.setChecked(false);
                }else{
                    houseType="";
                }
            }
        });
        mCb_xiezilou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    houseType ="1";
                    mCb_all.setChecked(false);
                    mCb_soho.setChecked(false);
                }else{
                    houseType ="";
                }
            }
        });
        mCb_soho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    houseType ="2";
                    mCb_xiezilou.setChecked(false);
                    mCb_all.setChecked(false);
                }else{
                    houseType ="";
                }
            }
        });
        //租售类型
        mCb_rent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rentType ="1";
                    mCb_sell.setChecked(false);
                }else{
                    if(mCb_sell.isChecked()){
                        rentType ="2";
                    }else{
                        rentType ="";
                    }

                }
                TsUtils.show(rentType);
            }
        });
        mCb_sell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rentType ="2";
                    mCb_rent.setChecked(false);
                }else{
                    if(mCb_rent.isChecked()){
                        rentType ="1";
                    }else{
                        rentType ="";
                    }
                }
                TsUtils.show(rentType);

            }
        });
    }
    private void initCustomOptionPicker() {
        //条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = units[options1];
                priceUnit=options1+"";
                mTv_unit.setText(tx);

            }
        })
                .setLayoutRes(R.layout.pup_company_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_ensure);
                        final TextView tv_cancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                        tv_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });


                    }
                })
                .isDialog(false)
                .build();

        pvCustomOptions.setPicker(Arrays.asList(units));//添加数据

    }
    private OnClickListenerWrapper mOnClickListener = new OnClickListenerWrapper() {
        @Override
        protected void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_reset:
                case R.id.btn_ok:
                    String startPrice =mEt_startprice.getText().toString();
                    String endPrice =mEt_endprice.getText().toString();
                    String startArea =mEt_startarea.getText().toString();
                    String endArea =mEt_endarea.getText().toString();
                    if("".equals(startPrice)&& !"".equals(endPrice)){
                        TsUtils.show("请填写完整的价格区域");
                        return;
                    }
                    if(!"".equals(startPrice)&& "".equals(endPrice)){
                        TsUtils.show("请填写完整的价格区域");
                        return;
                    }
                    if(!"".equals(startPrice) && !"".equals(endPrice)){
                        if(Integer.valueOf(startPrice)>Integer.valueOf(endPrice)){
                            TsUtils.show("最低价不能高于最高价");
                            return;
                        }
                    }

                    if(!"".equals(startArea)&&"".equals(endArea)){
                        TsUtils.show("请填写完整的面积区域");
                        return;
                    }
                    if("".equals(startArea)&&!"".equals(endArea)){
                        TsUtils.show("请填写完整的面积区域");
                        return;
                    }
                    if(!"".equals(startArea) && !"".equals(endArea)){
                        if(Integer.valueOf(startArea)>Integer.valueOf(endArea)){
                            TsUtils.show("最低面积不能高于最高面积");
                            return;
                        }
                    }
                    MyHouseListEvent myHouseListEvent =new MyHouseListEvent();
//                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
//                    myBuildListReq.msgReq =msgReqWithToken;
                    myHouseListEvent.pageNo =1; //页码
                    myHouseListEvent.pageSize =15;
                    myHouseListEvent.requestType ="";//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
                    myHouseListEvent.prov =prov;
                    myHouseListEvent.city =city;
                    myHouseListEvent.district =district;
                    myHouseListEvent.estateId =estateId;
                    myHouseListEvent.houseType =houseType; //房源类型 1:写字楼 2:酒店式soho
                    myHouseListEvent.rentType =rentType;//租售类型
                    myHouseListEvent.startPrice =startPrice; //最低价格
                    myHouseListEvent.endPrice =endPrice; //最高价格
                    myHouseListEvent.priceUnit =priceUnit; //价格单位
                    myHouseListEvent.startArea =startArea; //最低面积
                    myHouseListEvent.endArea =endArea;    //最高面积
                    myHouseListEvent.decoration =decoration; //装修情况
                    myHouseListEvent.provideRentFree =provideRentFree; //免租期
                    myHouseListEvent.houseName ="";   //房源名称

                    EventBus.getDefault().post(myHouseListEvent);
                    menuCallBack.setupCloseMean();
                    break;
                case R.id.lin_address_content:
                    ShowPickerView();
                    break;
                case R.id.lin_address:
                    break;
                case R.id.lin_building:
                    if(mRv_buildlist.getVisibility()==VISIBLE){
                        mRv_buildlist.setVisibility(GONE);
                    }else{
                        mRv_buildlist.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_buildingtypetitle:
                    if(mLin_buildingType.getVisibility()==VISIBLE){
                        mLin_buildingType.setVisibility(GONE);
                    }else{
                        mLin_buildingType.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_houseprice:
                    if(mLin_priceRange.getVisibility()==VISIBLE){
                        mLin_priceRange.setVisibility(GONE);
                    }else{
                        mLin_priceRange.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_housearea:
                    if(mLin_arearange.getVisibility()==VISIBLE){
                        mLin_arearange.setVisibility(GONE);
                    }else{
                        mLin_arearange.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_type:
                    if(mLin_type_select.getVisibility()==VISIBLE){
                        mLin_type_select.setVisibility(GONE);
                    }else{
                        mLin_type_select.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_happening:
                    if(mLin_decoration.getVisibility()==VISIBLE){
                        mLin_decoration.setVisibility(GONE);
                    }else{
                        mLin_decoration.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.lin_mianzu:
                    if(mLin_mianzudate.getVisibility()==VISIBLE){
                        mLin_mianzudate.setVisibility(GONE);
                    }else{
                        mLin_mianzudate.setVisibility(VISIBLE);
                    }
                    break;
                case R.id.tv_unit:
                    pvCustomOptions.show();
                    break;
            }
        }
    };

    private FilterView.CloseMenuCallBack menuCallBack;

    public interface CloseMenuCallBack {
        void setupCloseMean();
    }

    public void setCloseMenuCallBack(FilterView.CloseMenuCallBack menuCallBack) {
        this.menuCallBack = menuCallBack;
    }
    // 弹出选择器
    private void ShowPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = proviceItems.get(options1).getPickerViewText() +
                        cityItems.get(options1).get(options2)+areaItems.get(options1).get(options2).get(options3);
                areas = proviceItems.get(options1).childRegions.get(options2).childRegions;
                cityId =areas.get(options3).id+"";
//                Toast.makeText(CertificationActivity.this,tx,Toast.LENGTH_SHORT).show();
                prov =proviceItems.get(options1).id;
                city =proviceItems.get(options1).childRegions.get(options2).id;
                district =proviceItems.get(options1).childRegions.get(options2).childRegions.get(options3).id;
                mTv_area.setText(tx);
                getData(1,cityId,"");
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
        HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
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
                        .execute(new JsonCallBack<BaseBean<HttpResponseStruct.AreaData>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.AreaData>> response) {
                                if("00000".equals(response.body().getResultCode())){
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

                            }
                        });

            }
        });
    }
    public void getData(final int page, final String cityId, final String estateName){
        final long date = new Date().getTime();
        final String number= HttpUtil.getRandom();
        HttpUtil.getSignature(mContext, date+"", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.MyBuildsListReq myBuildListReq =new HttpRequestStruct.MyBuildsListReq();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                myBuildListReq.msgReq =msgReqWithToken;
                myBuildListReq.pageNo =page;
                myBuildListReq.pageSize =20;
                myBuildListReq.cityId =cityId;
                myBuildListReq.estateName =estateName;
                String json = gson.toJson(myBuildListReq);
                OkGo.<BaseBean<HttpResponseStruct.UserBuildListData>>post(InterfaceUrl.MY_BUILD_LIST)
                        .tag(mContext)
                        .upJson(json)
                        .execute(new JsonCallBack<BaseBean<HttpResponseStruct.UserBuildListData>>() {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserBuildListData>> response) {
                                if (!"00000".equals(response.body().getResultCode())) {
                                    TsUtils.show(response.body().getResultMsg());
                                    return;
//                                    mSmartRefresh.finishRefresh();
//                                    mSmartRefresh.finishLoadmore();
                                }
                                HttpResponseStruct.BuildListPage pageData =response.body().getData().userEstates;
                                if (pageData!= null) {
                                    pageCount = pageData.totalPage;
                                    listDatas =pageData.dataList;
                                    if (listDatas != null && listDatas.size() < 10) {
                                        if (page == 1) {
                                            buildsAdapter.setNewData(listDatas);
                                            pageNum = 1;
                                        } else {
                                            buildsAdapter.addData(listDatas);
                                        }
//                                        mSmartRefresh.finishRefresh();
//                                        mSmartRefresh.finishLoadmore();
                                    } else {
                                        if (listDatas == null) {
//                                            buildsAdapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_buildlist.getParent());
                                            return;
                                        }
                                        if (page == 1) {
                                            buildsAdapter.setNewData(listDatas);
                                        } else {
                                            if (listDatas != null && listDatas.size() != 0) {
                                                buildsAdapter.addData(listDatas);
                                            }
                                        }
//                                        mSmartRefresh.finishRefresh();
//                                        mSmartRefresh.finishLoadmore();
                                    }
                                } else {
                                    if (page == 1) {
                                        buildsAdapter.setNewData(null);
//                                        buildsAdapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_buildlist.getParent());
                                    } else {
                                        buildsAdapter.notifyDataSetChanged();
                                    }
//                                    mSmartRefresh.finishRefresh();
                                }
                                buildsAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }
    class  DecorationAdapter extends BaseQuickAdapter<RentBean,BaseViewHolder>{
        private int mSelectedPos = 0;   //实现单选，保存当前选中的position
        private List<RentBean> list;
        public DecorationAdapter(@Nullable List<RentBean> data) {
            super(R.layout.item_filter_type, data);
            this.list = data;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelect) {
                    mSelectedPos = i;
                }
            }
        }

        @Override
        protected void convert(final BaseViewHolder helper, final RentBean item) {
            CheckBox checkBox =helper.getView(R.id.checkbox);
            final int position = helper.getAdapterPosition();
            checkBox.setChecked(item.isSelect);
            checkBox.setText(item.title);
            checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(mSelectedPos).isSelect=false;
                    //设置新的Item勾选状态
                    mSelectedPos = position;
                    list.get(mSelectedPos).isSelect=true;

                    if("全部".equals(item.title)){
                        decoration="";
                    }else if("简装修".equals(item.title)){
                        decoration="1";
                    }else if("毛坯".equals(item.title)){
                        decoration="2";
                    }else if("精装修".equals(item.title)){
                        decoration="3";
                    }else if("豪华装修".equals(item.title)){
                        decoration="4";
                    }
                    TsUtils.show(decoration+"");
                    notifyDataSetChanged();
                }
            });

        }

    }
    class  RentsAdapter extends BaseQuickAdapter<RentBean,BaseViewHolder>{
        private int mSelectedPos = 0;   //实现单选，保存当前选中的position
        private List<RentBean> list;
        public RentsAdapter(@Nullable List<RentBean> data) {
            super(R.layout.item_filter_type, data);
            this.list = data;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelect) {
                    mSelectedPos = i;
                }
            }
        }

        @Override
        protected void convert(final BaseViewHolder helper, final RentBean item) {
            CheckBox checkBox =helper.getView(R.id.checkbox);
            final int position = helper.getAdapterPosition();
            checkBox.setChecked(item.isSelect);
            checkBox.setText(item.title);
            checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(mSelectedPos).isSelect=false;
                    //设置新的Item勾选状态
                    mSelectedPos = position;
                    list.get(mSelectedPos).isSelect=true;

                    if("全部".equals(item.title)){
                        provideRentFree="";
                    }else if("暂无".equals(item.title)){
                        provideRentFree="1";
                    }else if("一周".equals(item.title)){
                        provideRentFree="2";
                    }else if("半个月".equals(item.title)){
                        provideRentFree="3";
                    }else if("二十天".equals(item.title)){
                        provideRentFree="4";
                    }else if("一个月".equals(item.title)){
                        provideRentFree="5";
                    }else if("二个月".equals(item.title)){
                        provideRentFree="6";
                    }else if("三个月".equals(item.title)){
                        provideRentFree="7";
                    }else if("四个月".equals(item.title)){
                        provideRentFree="8";
                    }else if("五个月".equals(item.title)){
                        provideRentFree="9";
                    }else if("半年".equals(item.title)){
                        provideRentFree="10";
                    }
                    TsUtils.show(provideRentFree+"");
                    notifyDataSetChanged();
                }
            });

        }

    }
    class  BuildsAdapter extends BaseQuickAdapter<HttpResponseStruct.BuildSimpleDetail,BaseViewHolder>{
        private int mSelectedPos = 0;   //实现单选，保存当前选中的position
        private List<HttpResponseStruct.BuildSimpleDetail> list;
        public BuildsAdapter(@Nullable List<HttpResponseStruct.BuildSimpleDetail> data) {
            super(R.layout.item_filter_type, data);
            this.list = data;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelect) {
                    mSelectedPos = i;
                }
            }
        }

        @Override
        protected void convert(final BaseViewHolder helper, final HttpResponseStruct.BuildSimpleDetail item) {
            CheckBox checkBox =helper.getView(R.id.checkbox);
//            final int position = helper.getAdapterPosition();
            checkBox.setChecked(item.isSelect);
            checkBox.setText(item.estateName);
            checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listDatas.get(mSelectedPos).isSelect){
                        listDatas.get(mSelectedPos).isSelect=true;
                        //设置新的Item勾选状态
                        mSelectedPos = helper.getAdapterPosition();
                        listDatas.get(mSelectedPos).isSelect=false;

                        estateId ="";//楼盘id
                        TsUtils.show(estateId+""+"mSelectedPos"+mSelectedPos);
                        notifyDataSetChanged();
                    }else{
                        listDatas.get(mSelectedPos).isSelect=false;
                        //设置新的Item勾选状态
                        mSelectedPos = helper.getAdapterPosition();
                        listDatas.get(mSelectedPos).isSelect=true;

                        estateId =item.id+"";//楼盘id
                        TsUtils.show(estateId+""+"mSelectedPos"+mSelectedPos);
                        notifyDataSetChanged();
                    }

                }
            });

        }

    }

}
