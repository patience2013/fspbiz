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
import com.fangshang.fspbiz.bean.SearchMyBuildEvent;
import com.fangshang.fspbiz.fragment.housing.build.UserBuildDetailActivity;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.GlideLoaderUtil;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.OperatingDialog;
import com.fangshang.fspbiz.util.TsUtils;
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

public class BuildListFragment extends BaseFragment {
    @BindView(R.id.rv_houselist)
    RecyclerView mRv_houselist;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mSmartRefresh;

    private Gson gson =new Gson();
    private int pageNum=1;
    private int pageCount;

    private List<HttpResponseStruct.BuildSimpleDetail> listDatas =new ArrayList<>();
    private ListAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buildlist;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //刷新控件的下拉监听
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                listDatas.clear();
                mSmartRefresh.setLoadmoreFinished(false);
                getData(pageNum,"","");
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
                getData(pageNum,"","");
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_houselist.setLayoutManager(layoutManager);

        getData(pageNum,"","");
        initAdapter();
    }

    public void initAdapter() {
        adapter = new ListAdapter(listDatas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//数据加载动画
//        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRv_list.getParent());
        mRv_houselist.setAdapter(adapter);
    }

    class ListAdapter extends BaseQuickAdapter<HttpResponseStruct.BuildSimpleDetail,BaseViewHolder>{

        public ListAdapter(@Nullable List<HttpResponseStruct.BuildSimpleDetail> data) {
            super(R.layout.item_mybuildlist, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final HttpResponseStruct.BuildSimpleDetail item) {
            ImageView imageView =helper.getView(R.id.img_list);
            GlideLoaderUtil.display(mContext,imageView,item.pic+"!thumb");
            helper.setText(R.id.tv_title,item.estateName)
                    .setText(R.id.tv_area,"面积范围  "+item.startArea+"-"+item.endArea+"㎡")
                    .setText(R.id.tv_price,item.startPrice);
            //处于“已通过”状态的楼盘信息，没有“修改”功能按钮
            //处于“审核中”状态的楼盘信息，才有“催审”功能按钮
            //处于“未通过”状态的楼盘信息，才有“修改”功能按钮
            TextView tv_type =helper.getView(R.id.tv_type);
            if(item.unit==1){
                tv_type.setText("元/m²/天起");
            }else if(item.unit==2){
                tv_type.setText("元/m²/月起");
            }else if(item.unit==3){
                tv_type.setText("元/月起");
            }else if(item.unit==4){
                tv_type.setText("元/m²/天");
            }else if(item.unit==5){
                tv_type.setText("元/m²/月");
            }else if(item.unit==6){
                tv_type.setText("元/月");
            }else if(item.unit==7){
                tv_type.setText("元/m²");
            }else if(item.unit==8){
                tv_type.setText("元");
            }else if(item.unit==9){
                tv_type.setText("元/工位/月");
            }else if(item.unit==10){
                tv_type.setText("元/间/月");
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserBuildDetailActivity.actionStart(mContext,0,2);//用户楼盘详情
                }
            });
            helper.getView(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddHouseActivity.actionStart(mContext,item.id+"",item.estateName,"");
                }
            });
            helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final OperatingDialog dialog =new OperatingDialog(mContext,"确定删除楼盘？");
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
                                    HttpRequestStruct.BuildDeleteReq buildDeleteReq=new HttpRequestStruct.BuildDeleteReq();
                                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                                    buildDeleteReq.msgReq =msgReqWithToken;
                                    buildDeleteReq.estateId=item.id;
                                    String json =gson.toJson(buildDeleteReq);
                                    OkGo.<BaseBean<HttpResponseStruct.DelHouseData>>post(InterfaceUrl.DELETE_BUILD)
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
                        }
                    });
                    dialog.show();
                }
            });
            //查看房源
            helper.getView(R.id.tv_houselist).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HouseListActivity.actionStart(mContext);
                }
            });
        }
    }
    @OnClick(R.id.img_addbuild)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_addbuild:
                AddBuildActivity.actionStart(mContext,"");
                break;
        }
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
                myBuildListReq.pageSize =15;
                myBuildListReq.cityId =cityId;
                myBuildListReq.estateName =estateName;
                String json = gson.toJson(myBuildListReq);
                OkGo.<BaseBean<HttpResponseStruct.UserBuildListData>>post(InterfaceUrl.MY_BUILD_LIST)
                        .tag(mContext)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.UserBuildListData>>((Activity) mContext) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.UserBuildListData>> response) {
                                if (!"00000".equals(response.body().getResultCode())) {
                                    TsUtils.show(response.body().getResultMsg());
                                    mSmartRefresh.finishRefresh();
                                    mSmartRefresh.finishLoadmore();
                                    return;
                                }
                                HttpResponseStruct.BuildListPage pageData =response.body().getData().userEstates;
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
                                            listDatas =new ArrayList<>();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SearchMyBuildEvent event) {
        Logger.d("收到消息"+event.toString());
        getData(1,event.cityId,event.text);
    }
}
