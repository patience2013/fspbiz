package com.fangshang.fspbiz.fragment.housing.build;

import android.annotation.SuppressLint;
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
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.GlideImageLoader;
import com.fangshang.fspbiz.util.GlideLoaderUtil;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.TsUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 14:27
 */

public class MyReleaseBuildListFragment extends BaseFragment {
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.rv_buildlist)
    RecyclerView mRv_buildlist;

    private BuildlistAdapter adapter;
    private List<HttpResponseStruct.BuildSimpleDetail> listDatas =new ArrayList<>();
    private Gson gson =new Gson();
    private int pageNum=1;
    private int pageCount;
    private int approveSta=0;//审核状态0:全部 1:审核通过 2:审核中 3:审核失败
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myreleasebuildlist;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if(null!=bundle){
            approveSta =bundle.getInt("approveSta");
        }
        Logger.d(approveSta);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //刷新控件的下拉监听
        mSmartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                listDatas.clear();
                mSmartRefresh.setLoadmoreFinished(false);
                getData(1,approveSta);
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
                getData(pageNum,approveSta);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_buildlist.setLayoutManager(layoutManager);
        getData(pageNum,approveSta);
        initAdapter();
    }
    public void initAdapter() {
        adapter = new BuildlistAdapter(listDatas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//数据加载动画
//        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRv_list.getParent());
        mRv_buildlist.setAdapter(adapter);
    }
    class BuildlistAdapter extends BaseQuickAdapter<HttpResponseStruct.BuildSimpleDetail,BaseViewHolder>{

        public BuildlistAdapter(@Nullable List<HttpResponseStruct.BuildSimpleDetail> data) {
            super(R.layout.item_myreleasebuildlist, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final HttpResponseStruct.BuildSimpleDetail item) {
            TextView mTv_update =helper.getView(R.id.tv_update);
            TextView mTv_delete =helper.getView(R.id.tv_delete);
            TextView mTv_cuishen =helper.getView(R.id.tv_cuishen);


            ImageView img_status =helper.getView(R.id.img_status);
            if(item.approveSta==0){
                img_status.setImageResource(R.drawable.icon_notthrough);
            }else if(item.approveSta==1){
                img_status.setImageResource(R.drawable.icon_through);
                mTv_delete.setVisibility(View.VISIBLE);
                mTv_cuishen.setVisibility(View.GONE);
                mTv_update.setVisibility(View.GONE);
            }else if(item.approveSta==2){
                img_status.setImageResource(R.drawable.icon_auditing);
                mTv_delete.setVisibility(View.GONE);
                mTv_cuishen.setVisibility(View.VISIBLE);
                mTv_update.setVisibility(View.GONE);
            }else if(item.approveSta==3){
                img_status.setImageResource(R.drawable.icon_notthrough);
                mTv_delete.setVisibility(View.VISIBLE);
                mTv_cuishen.setVisibility(View.GONE);
                mTv_update.setVisibility(View.VISIBLE);
            }

            ImageView imageView =helper.getView(R.id.img_build);
            GlideLoaderUtil.display(mContext,imageView,item.approveEstatePicture);
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
                    UserBuildDetailActivity.actionStart(mContext,item.id,1);
                }
            });
            mTv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show("修改");
                }
            });
            mTv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show("删除");
                }
            });
            mTv_cuishen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TsUtils.show("催审");
                }
            });

        }
    }

    public void getData(final int page, final int approveSta){
        final long date = new Date().getTime();
        final String number= HttpUtil.getRandom();
        HttpUtil.getSignature(mContext, date+"", number, new HttpUtil.NetListener() {
            @Override
            public void success(String sign) {
                HttpRequestStruct.MyBuildListReq myBuildListReq =new HttpRequestStruct.MyBuildListReq();
                HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android",AccountHelper.getUser().getToken(),sign,date+"",number);
                myBuildListReq.msgReq =msgReqWithToken;
                myBuildListReq.pageNo =page;
                myBuildListReq.pageSize =15;
                myBuildListReq.approveSta =approveSta;
                String json = gson.toJson(myBuildListReq);
                OkGo.<BaseBean<HttpResponseStruct.BuildListData>>post(InterfaceUrl.MY_RELEASEBUILD_LIST)
                        .tag(this)
                        .upJson(json)
                        .execute(new DialogCallback<BaseBean<HttpResponseStruct.BuildListData>>((Activity) mContext) {
                            @Override
                            public void onSuccess(Response<BaseBean<HttpResponseStruct.BuildListData>> response) {
                                if (!"00000".equals(response.body().getResultCode())) {
                                    TsUtils.show(response.body().getResultMsg());
                                    mSmartRefresh.finishRefresh();
                                    mSmartRefresh.finishLoadmore();
                                    return;
                                }
                                HttpResponseStruct.BuildListPage pageData =response.body().getData().page;
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
                                            adapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_buildlist.getParent());
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
                                        adapter.setEmptyView(R.layout.empty_public_view, (ViewGroup) mRv_buildlist.getParent());
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
}
