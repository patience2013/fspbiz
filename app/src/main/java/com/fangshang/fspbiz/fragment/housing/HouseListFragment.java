package com.fangshang.fspbiz.fragment.housing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.bean.MyHouseListEvent;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.GlideLoaderUtil;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.OperatingDialog;
import com.fangshang.fspbiz.util.TsUtils;
import com.fangshang.fspbiz.util.ZhuanHuanUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/8/008 15:14
 */

public class HouseListFragment extends BaseFragment {
    @BindView(R.id.rv_houselist)
    RecyclerView mRv_houselist;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private int requestType;

    Gson gson =new Gson();
    private int pageNum=1;
    private int pageCount;

    private List<HttpResponseStruct.HouseSimpleDetail> listDatas =new ArrayList<HttpResponseStruct.HouseSimpleDetail>();

    private ListAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_houselist;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null !=bundle){
            requestType=bundle.getInt("requestType");
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //注册EventBus
        EventBus.getDefault().register(this);
        //刷新控件的下拉监听
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                listDatas.clear();
                mSmartRefresh.setLoadmoreFinished(false);
                getData(pageNum,requestType+"",0,0,0,"","","","","","","","","","","");

            }
        });
        mSmartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (pageNum >= pageCount) {
                    TsUtils.show("已是最后一页");
//                    hideRefreshView();
                    mSmartRefresh.finishLoadmore();
                    mSmartRefresh.setLoadmoreFinished(true);
                    return;
                }
                pageNum++;
//                getData(pageNum,"","");
                getData(pageNum,requestType+"",0,0,0,"","","","","","","","","","","");


            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_houselist.setLayoutManager(layoutManager);

//        getData(pageNum,"","");
        getData(pageNum,requestType+"",0,0,0,"","","","","","","","","","","");

        initAdapter();

    }
    @OnClick()
    public void onClick(View view){
        switch (view.getId()){

        }
    }
    public void initAdapter() {
        adapter = new ListAdapter(listDatas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//数据加载动画
//        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRv_list.getParent());
        mRv_houselist.setAdapter(adapter);
    }


    public void getData(final int page, final String requestType, final int prov, final int city, final int district, final String estateId, final String houseType, final String rentType, final String startPrice, final String endPrice, final String priceUnit, final String startArea, final String endArea, final String decoration, final String provideRentFree, final String houseName){
        final long date = new Date().getTime();
        final String number= HttpUtil.getRandom();
        HttpUtil.getSignature(mContext, date+"", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.MyHousesListReq myBuildListReq =new HttpRequestStruct.MyHousesListReq();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                myBuildListReq.msgReq =msgReqWithToken;
                myBuildListReq.pageNo =page;
                myBuildListReq.pageSize =15;
                myBuildListReq.requestType =requestType;
                myBuildListReq.prov =prov;
                myBuildListReq.city =city;
                myBuildListReq.district =district;
                myBuildListReq.estateId =estateId;
                myBuildListReq.houseType =houseType; //房源类型 1:写字楼 2:酒店式soho
                myBuildListReq.rentType =rentType;
                myBuildListReq.startPrice =startPrice;
                myBuildListReq.endPrice =endPrice;
                myBuildListReq.priceUnit =priceUnit;
                myBuildListReq.startArea =startArea;
                myBuildListReq.endArea =endArea;
                myBuildListReq.decoration =decoration;
                myBuildListReq.provideRentFree =provideRentFree;
                myBuildListReq.houseName =houseName;

//                myBuildListReq.requestType =cityId;
//                myBuildListReq.estateName =estateName;
                String json = gson.toJson(myBuildListReq);
                OkGo.<BaseBean<HttpResponseStruct.HouseListData>>post(InterfaceUrl.MY_HOUSE_LIST)
                        .tag(mContext)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.HouseListData>>((Activity) mContext) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.HouseListData>> response) {
                                if (!"00000".equals(response.body().getResultCode())) {
                                    TsUtils.show(response.body().getResultMsg());
                                    mSmartRefresh.finishRefresh();
                                    mSmartRefresh.finishLoadmore();
                                    return;
                                }
                                HttpResponseStruct.HouseListPage pageData =response.body().getData().page;
                                if (pageData!= null) {
                                    pageCount = pageData.totalPage;
                                    listDatas =pageData.dataList;
                                    if (listDatas != null && listDatas.size() < 10) {
                                        if (page == 1) {
                                            adapter.setNewData(listDatas);
                                            pageNum = 1;
                                        } else {
                                            adapter.addData(listDatas);
                                        }
                                        mSmartRefresh.finishRefresh();
                                        mSmartRefresh.finishLoadmore();
                                    } else {
                                        if (listDatas == null) {
                                            listDatas=new ArrayList<>();
                                            adapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_houselist.getParent());
                                            mSmartRefresh.finishRefresh();
                                            mSmartRefresh.finishLoadmore();
                                            return;
                                        }
                                        if (page == 1) {
                                            adapter.setNewData(listDatas);
                                        } else {
                                            if (listDatas != null && listDatas.size() != 0) {
                                                adapter.addData(listDatas);
                                            }
                                        }
                                        mSmartRefresh.finishRefresh();
                                        mSmartRefresh.finishLoadmore();
                                    }
                                } else {
                                    if (page == 1) {
                                        adapter.setNewData(null);
                                        adapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_houselist.getParent());
                                    } else {
                                        adapter.notifyDataSetChanged();
                                    }
                                    mSmartRefresh.finishRefresh();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }

    class ListAdapter extends BaseQuickAdapter<HttpResponseStruct.HouseSimpleDetail,BaseViewHolder>{

        public ListAdapter(@Nullable List<HttpResponseStruct.HouseSimpleDetail> data) {
            super(R.layout.item_houselist, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final HttpResponseStruct.HouseSimpleDetail item) {
            TextView mTv_push=helper.getView(R.id.tv_push);
            TextView mTv_refresh=helper.getView(R.id.tv_refresh);
            final TextView mTv_downorup=helper.getView(R.id.tv_downorup);
            TextView mTv_update=helper.getView(R.id.tv_update);
            TextView mTv_delete=helper.getView(R.id.tv_delete);
            TextView mTv_cuishen=helper.getView(R.id.tv_cuishen);
            TextView mTv_restore=helper.getView(R.id.tv_restore);
            TextView mTv_really_delete=helper.getView(R.id.tv_really_delete);

            //是否上架
            if(item.isOnSale==1){
                mTv_push.setVisibility(View.VISIBLE);
                mTv_refresh.setVisibility(View.VISIBLE);
                mTv_downorup.setVisibility(View.VISIBLE);
                mTv_downorup.setText("下架");
                mTv_update.setVisibility(View.GONE);
                mTv_delete.setVisibility(View.GONE);
                mTv_cuishen.setVisibility(View.GONE);
                mTv_restore.setVisibility(View.GONE);
                mTv_really_delete.setVisibility(View.GONE);
            }else if(item.isOnSale==0){
                if(item.approveSta==1){//审核通过
                    mTv_push.setVisibility(View.GONE);
                    mTv_refresh.setVisibility(View.GONE);
                    mTv_downorup.setVisibility(View.VISIBLE);
                    mTv_downorup.setText("上架");
                    mTv_update.setVisibility(View.VISIBLE);
                    mTv_delete.setVisibility(View.VISIBLE);
                    mTv_cuishen.setVisibility(View.GONE);
                    mTv_restore.setVisibility(View.GONE);
                    mTv_really_delete.setVisibility(View.GONE);
                }else if(item.approveSta==2){//审核中
                    mTv_push.setVisibility(View.GONE);
                    mTv_refresh.setVisibility(View.GONE);
                    mTv_downorup.setVisibility(View.GONE);
//                    mTv_downorup.setText("上架");
                    mTv_update.setVisibility(View.GONE);
                    mTv_delete.setVisibility(View.GONE);
                    mTv_cuishen.setVisibility(View.VISIBLE);
                    mTv_restore.setVisibility(View.GONE);
                    mTv_really_delete.setVisibility(View.GONE);
                }else if(item.approveSta==3){//审核未通过
                    mTv_push.setVisibility(View.GONE);
                    mTv_refresh.setVisibility(View.GONE);
                    mTv_downorup.setVisibility(View.GONE);
//                    mTv_downorup.setText("上架");
                    mTv_update.setVisibility(View.VISIBLE);
                    mTv_delete.setVisibility(View.VISIBLE);
                    mTv_cuishen.setVisibility(View.GONE);
                    mTv_restore.setVisibility(View.GONE);
                    mTv_really_delete.setVisibility(View.GONE);
                }//少了删除状态
                if(requestType==5){
                    mTv_push.setVisibility(View.GONE);
                    mTv_refresh.setVisibility(View.GONE);
                    mTv_downorup.setVisibility(View.GONE);
//                    mTv_downorup.setText("上架");
                    mTv_update.setVisibility(View.GONE);
                    mTv_delete.setVisibility(View.GONE);
                    mTv_cuishen.setVisibility(View.GONE);
                    mTv_restore.setVisibility(View.VISIBLE);
                    mTv_really_delete.setVisibility(View.VISIBLE);
                }
            }

            ImageView imageView =helper.getView(R.id.img_house);
            GlideLoaderUtil.display(mContext,imageView,item.pic+"!thumb");
            helper.setText(R.id.tv_title,item.building+"幢"+item.floor+"层"+item.houeNo+"室")
//                    .setText(R.id.tv_area,"面积"+item.area+"㎡")
                    .setText(R.id.tv_push_num,"推送次数  "+item.pushAmount+"次")
                    .setText(R.id.tv_price,item.price)
                    .setText(R.id.tv_area,"面积:  "+item.area);
            TextView tv_decoration =helper.getView(R.id.tv_decoration);
            if("1".equals(item.decoration)){
                tv_decoration.setText("装修  简装");
            }else if("2".equals(item.decoration)){
                tv_decoration.setText("装修  毛坯");
            }else if("3".equals(item.decoration)){
                tv_decoration.setText("装修  精装修");
            }else if("4".equals(item.decoration)){
                tv_decoration.setText("装修  豪华装修");
            }
            //处于“已通过”状态的楼盘信息，没有“修改”功能按钮
            //处于“审核中”状态的楼盘信息，才有“催审”功能按钮
            //处于“未通过”状态的楼盘信息，才有“修改”功能按钮
            TextView tv_type =helper.getView(R.id.tv_type);

            if(item.unit==1){
                tv_type.setText("元/m²/天");
            }else if(item.unit==2){
                tv_type.setText("元/m²/月");
            }else if(item.unit==3){
                tv_type.setText("元/月");
            }else if(item.unit==4){
                tv_type.setText("元/m²");
            }else if(item.unit==5){
                tv_type.setText("元");
            }
            if(null!=item.refreshTime){
                helper.setText(R.id.tv_time, ZhuanHuanUtil.getTimes(item.refreshTime));
            }

//            GlideLoaderUtil.display(mContext,imageView,"/fsp/image/201801/23/1516708948597.png!thumb");
//            if()
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HouseDetailActivity.actionStart(mContext,item.id,2);
                }
            });

            helper.getView(R.id.tv_push).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show("推房源");
                }
            });
            helper.getView(R.id.tv_downorup).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text ="";
                    if("上架".equals(mTv_downorup)){
                        text ="确定上架房源？";
                    }else{
                        text ="确定下架房源？";
                    }
                    final OperatingDialog dialog =new OperatingDialog(mContext,text);
                    dialog.setNoOnclickListener(new OperatingDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setYesOnclickListener(new OperatingDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            final long date = new Date().getTime();
                            final String number= HttpUtil.getRandom();
                            HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
                                @Override
                                public void success(String sign) {
                                    HttpRequestStruct.HouseUpOrDownReq myHouseDetailReq=new HttpRequestStruct.HouseUpOrDownReq();
                                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                                    myHouseDetailReq.msgReq =msgReqWithToken;
                                    myHouseDetailReq.houseId=item.id;
                                    if("上架".equals(mTv_downorup.getText().toString())){
                                        myHouseDetailReq.opType =1;
                                    }else if("下架".equals(mTv_downorup.getText().toString())){
                                        myHouseDetailReq.opType =0;
                                    }
                                    myHouseDetailReq.opType =1; //操作类型 1：上架 0：下架
                                    String json =gson.toJson(myHouseDetailReq);
                                    OkGo.<BaseBean<HttpResponseStruct.DelHouseData>>post(InterfaceUrl.HOUSE_UPORDOWN)
                                            .tag(mContext)
                                            .upJson(json)
                                            .execute(new DialogCallback<BaseBean<HttpResponseStruct.DelHouseData>>((Activity) mContext) {
                                                @Override
                                                public void onSuccess(Response<BaseBean<HttpResponseStruct.DelHouseData>> response) {
                                                    if("00000".equals(response.body().getResultCode())){
                                                        if(response.body().getData().flag){
                                                            TsUtils.show("操作成功");
                                                        }else{
                                                            TsUtils.show("操作失败");
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
            helper.getView(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OperatingDialog dialog =new OperatingDialog(mContext,"确定刷新房源？");
                    dialog.setNoOnclickListener(new OperatingDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setYesOnclickListener(new OperatingDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            final long date = new Date().getTime();
                            final String number= HttpUtil.getRandom();
                            HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
                                @Override
                                public void success(String sign) {
                                    HttpRequestStruct.MyHouseDetailReq myHouseDetailReq=new HttpRequestStruct.MyHouseDetailReq();
                                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                                    myHouseDetailReq.msgReq=msgReqWithToken;
                                    myHouseDetailReq.houseId=item.id;
                                    String json =gson.toJson(myHouseDetailReq);
                                    OkGo.<BaseBean<HttpResponseStruct.DelHouseData>>post(InterfaceUrl.REFRESH_HOUSE)
                                            .tag(mContext)
                                            .upJson(json)
                                            .execute(new DialogCallback<BaseBean<HttpResponseStruct.DelHouseData>>((Activity) mContext) {
                                                @Override
                                                public void onSuccess(Response<BaseBean<HttpResponseStruct.DelHouseData>> response) {
                                                    if("00000".equals(response.body().getResultCode())){
                                                        if(response.body().getData().flag){
                                                            TsUtils.show("刷新成功");
                                                        }else{
                                                            TsUtils.show("刷新失败");
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
            helper.getView(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show("修改");
                    AddHouseActivity.actionStart(mContext,item.estateId+"",item.estateName+"",item.id+"");
                }
            });
            helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OperatingDialog dialog =new OperatingDialog(mContext,"确定删除房源？");
                    dialog.setNoOnclickListener(new OperatingDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setYesOnclickListener(new OperatingDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            final long date = new Date().getTime();
                            final String number= HttpUtil.getRandom();
                            HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
                                @Override
                                public void success(String sign) {
                                    HttpRequestStruct.HouseDeleteReq houseDeleteReq=new HttpRequestStruct.HouseDeleteReq();
                                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                                    houseDeleteReq.msgReq =msgReqWithToken;
                                    houseDeleteReq.houseId=item.id;
                                    houseDeleteReq.isDeleteCompletely =0;
                                    String json =gson.toJson(houseDeleteReq);
                                    OkGo.<BaseBean<HttpResponseStruct.DelHouseData>>post(InterfaceUrl.DELETE_HOUSE)
                                            .tag(mContext)
                                            .upJson(json)
                                            .execute(new DialogCallback<BaseBean<HttpResponseStruct.DelHouseData>>((Activity) mContext) {
                                                @Override
                                                public void onSuccess(Response<BaseBean<HttpResponseStruct.DelHouseData>> response) {
                                                    if("00000".equals(response.body().getResultCode())){
                                                        if(response.body().getData().flag){
                                                            TsUtils.show("删除成功");
                                                        }else{
                                                            TsUtils.show("失败失败");
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
            helper.getView(R.id.tv_really_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OperatingDialog dialog =new OperatingDialog(mContext,"确定彻底删除房源？");
                    dialog.setNoOnclickListener(new OperatingDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.setYesOnclickListener(new OperatingDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            final long date = new Date().getTime();
                            final String number= HttpUtil.getRandom();
                            HttpUtil.getSignature(mContext, date + "", number, new HttpUtil.NetListener() {
                                @Override
                                public void success(String sign) {
                                    HttpRequestStruct.HouseDeleteReq houseDeleteReq=new HttpRequestStruct.HouseDeleteReq();
                                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                                    houseDeleteReq.msgReq =msgReqWithToken;
                                    houseDeleteReq.houseId=item.id;
                                    houseDeleteReq.isDeleteCompletely =0;
                                    String json =gson.toJson(houseDeleteReq);
                                    OkGo.<BaseBean<HttpResponseStruct.DelHouseData>>post(InterfaceUrl.REFRESH_HOUSE)
                                            .tag(mContext)
                                            .upJson(json)
                                            .execute(new DialogCallback<BaseBean<HttpResponseStruct.DelHouseData>>((Activity) mContext) {
                                                @Override
                                                public void onSuccess(Response<BaseBean<HttpResponseStruct.DelHouseData>> response) {
                                                    if("00000".equals(response.body().getResultCode())){
                                                        if(response.body().getData().flag){
                                                            TsUtils.show("彻底删除成功");
                                                        }else{
                                                            TsUtils.show("彻底删除失败");
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyHouseListEvent event) {
//        String msg = "onEventMainThread收到了消息：" + event.getMessage();
//        Log.d("EventBus", msg);
        Logger.d("收到消息"+event.toString());
        getData(pageNum,requestType+"",event.prov,event.city,event.district,event.estateId,event.houseType,event.rentType,event.startPrice,event.endPrice,event.priceUnit,event.startArea,event.endArea,event.decoration,event.provideRentFree,event.houseName);

    }
}
