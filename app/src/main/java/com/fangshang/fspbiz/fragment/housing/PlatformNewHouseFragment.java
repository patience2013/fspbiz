package com.fangshang.fspbiz.fragment.housing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.weight.ScreeningMenu;
import com.fangshang.fspbiz.weight.ThreeSortHouseLinelayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/8/008 13:52
 */

public class PlatformNewHouseFragment extends BaseFragment {
    @BindView(R.id.dropDownMenu)
    ScreeningMenu mDropDownMenu;
    SmartRefreshLayout smartRefreshLayout;
    RecyclerView mRv_list;
    TextView mTv_all, mTv_out_of_date, mTv_less_than_ft, mTv_less_than_ty, mTv_less_than_sy, mTv_less_than_ny;
    private String headers[] = {"房源区域", "今天"};
    private List<View> popupViews = new ArrayList<>();
    private ListAdapter adapter;
    private List<String> data =new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_platform_newhouse;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        for (int i = 0; i <15 ; i++) {
            data.add("tt");
        }

        LayoutInflater flater = LayoutInflater.from(mContext);
        View view = flater.inflate(R.layout.base_refresh, null);
        smartRefreshLayout = view.findViewById(R.id.smartRefresh);
        mRv_list = new RecyclerView(mContext);
        mRv_list.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg));
//        recycle.setBackgroundResource(R.color.white);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_list.setLayoutManager(layoutManager);
//        smartRefesh.addView(recycle);
        smartRefreshLayout.addView(mRv_list, 1);

        //刷新控件的下拉监听
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                pageNum = 1;
//                listDatas.clear();
                smartRefreshLayout.setLoadmoreFinished(false);
//                requestList();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
//                if (pageNum >= pageCount) {
//                    TsUtils.show(getString(R.string.it_is_the_last_page));
////                    hideRefreshView();
                    smartRefreshLayout.finishLoadmore();
                    smartRefreshLayout.setLoadmoreFinished(true);
//                    return;
//                }
//                pageNum++;
//                requestList();
            }
        });
        initAdapter();
        initMenu();
    }
    public void initAdapter() {
        View view = View.inflate(mContext,R.layout.line_head,null);
        adapter = new ListAdapter(data);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);//数据加载动画
//        adapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRv_list.getParent());
        adapter.addHeaderView(view);
        mRv_list.setAdapter(adapter);
    }
    private void initMenu() {
        ThreeSortHouseLinelayout sortLinelayout = new ThreeSortHouseLinelayout(mContext);
        sortLinelayout.setItemClickListener(new ThreeSortHouseLinelayout.OnItemClickListener() {
            @Override
            public void OnClick(String title, HttpResponseStruct.HouseIdTitle houseIdTitle) {
                mDropDownMenu.setTabText(title + "");
                mDropDownMenu.closeMenu();
//                pageNum = 1;
//                buildingId = houseIdTitle.buildingId;
//                floorId = houseIdTitle.floorId;
//                houseId = houseIdTitle.houseId;
////                remainDays="";
//                requestList();
            }
        });
        View view_status = LayoutInflater.from(mContext).inflate(R.layout.pop_filtrate, null);
        mTv_all = view_status.findViewById(R.id.tv_all);
        mTv_all.setOnClickListener(popListener);
        mTv_out_of_date = view_status.findViewById(R.id.tv_out_of_date);
        mTv_out_of_date.setOnClickListener(popListener);
        mTv_less_than_ft = view_status.findViewById(R.id.tv_less_than_ft);
        mTv_less_than_ft.setOnClickListener(popListener);
        mTv_less_than_ty = view_status.findViewById(R.id.tv_less_than_ty);
        mTv_less_than_ty.setOnClickListener(popListener);
        mTv_less_than_sy = view_status.findViewById(R.id.tv_less_than_sy);
        mTv_less_than_sy.setOnClickListener(popListener);
        mTv_less_than_ny = view_status.findViewById(R.id.tv_less_than_ny);
        mTv_less_than_ny.setOnClickListener(popListener);


        popupViews.add(sortLinelayout);
        popupViews.add(view_status);


        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, smartRefreshLayout);
    }
    //popupwindow内部控件的点击事件
    View.OnClickListener popListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //全部
                case R.id.tv_all:
                    mTv_all.setTextColor(Color.parseColor("#0BBFDE"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#626262"));
                    mDropDownMenu.setTabText("全部");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "";
//                    listDatas.clear();
//                    requestList();
                    break;
                //已过期
                case R.id.tv_out_of_date:
                    mTv_all.setTextColor(Color.parseColor("#626262"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#0BBFDE"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#626262"));
                    mDropDownMenu.setTabText("已过期");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "-1";
//                    listDatas.clear();
//                    requestList();
                    break;
                //到期日小于15天
                case R.id.tv_less_than_ft:
                    mTv_all.setTextColor(Color.parseColor("#626262"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#0BBFDE"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#626262"));
                    mDropDownMenu.setTabText("小于15天");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "15";
//                    listDatas.clear();
//                    requestList();
                    break;
                //到期日小于30天
                case R.id.tv_less_than_ty:
                    mTv_all.setTextColor(Color.parseColor("#626262"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#0BBFDE"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#626262"));
                    mDropDownMenu.setTabText("小于30天");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "30";
//                    listDatas.clear();
//                    requestList();
                    break;
                //到期日小于60天
                case R.id.tv_less_than_sy:
                    mTv_all.setTextColor(Color.parseColor("#626262"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#0BBFDE"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#626262"));
                    mDropDownMenu.setTabText("小于60天");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "60";
//                    listDatas.clear();
//                    requestList();
                    break;
                //到期日小于90天
                case R.id.tv_less_than_ny:
                    mTv_all.setTextColor(Color.parseColor("#626262"));
                    mTv_out_of_date.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ft.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ty.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_sy.setTextColor(Color.parseColor("#626262"));
                    mTv_less_than_ny.setTextColor(Color.parseColor("#0BBFDE"));
                    mDropDownMenu.setTabText("小于90天");
                    mDropDownMenu.closeMenu();
//                    listDatas.clear();
//                    pageNum = 1;
//                    remainDays = "90";
//                    listDatas.clear();
//                    requestList();
                    break;
                case R.id.background:
                    break;
                default:
                    break;
            }
        }
    };
    class ListAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        public ListAdapter(@Nullable List<String> data) {
            super(R.layout.item_platformnewhouse, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

        }
    }
}
