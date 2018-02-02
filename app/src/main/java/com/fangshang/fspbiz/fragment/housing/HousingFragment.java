package com.fangshang.fspbiz.fragment.housing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.fragment.housing.build.SearchHouseActivity;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.weight.FilterView;
import com.fangshang.fspbiz.weight.NoSlideViewPager;
import com.fangshang.fspbiz.weight.RightSideslipLay;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/6/006 17:25
 */

public class HousingFragment extends BaseFragment {
    @BindView(R.id.tv_building)
    TextView mTv_building;
    @BindView(R.id.lin_right)
    LinearLayout mLin_right;
    @BindView(R.id.slidingTab)
    SegmentTabLayout mSlidingTab;
    @BindView(R.id.vp_housemanage)
    NoSlideViewPager mVp_housemanage;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer_layout;
    @BindView(R.id.nav_view)
    LinearLayout navigationView;

    private RightSideslipLay menuHeaderView;
    private FilterView filterView;

    private List<Fragment> fraglist = new ArrayList<>();
    private String[] titles = new String[]{"我的房源", "平台新房源"};
    private FragmentManager manager;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_housing;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Logger.d(AccountHelper.getUser().getPhone());
        mDrawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        menuHeaderView = new RightSideslipLay(mContext);
        filterView =new FilterView(mContext);
        filterView.setCloseMenuCallBack(new FilterView.CloseMenuCallBack() {
            @Override
            public void setupCloseMean() {
                closeMenu();
            }
        });
//        navigationView.addView(menuHeaderView);
        navigationView.addView(filterView);
        menuHeaderView.setCloseMenuCallBack(new RightSideslipLay.CloseMenuCallBack() {
            @Override
            public void setupCloseMean() {
                closeMenu();
            }
        });

        manager =getFragmentManager();
        fraglist.add(new MyHouseFragment());
        fraglist.add(new PlatformNewHouseFragment());

//        mSlidingTab.setViewPager(mVp_housemanage,titles, (FragmentActivity) mContext, (ArrayList<Fragment>) fraglist);
        mSlidingTab.setTabData(titles);
        mVp_housemanage.setAdapter(new MyPagerAdapter(((FragmentActivity) mContext).getSupportFragmentManager()));
        mSlidingTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVp_housemanage.setCurrentItem(position);
                if(position==1){
                    mTv_building.setVisibility(View.INVISIBLE);
                    mLin_right.setVisibility(View.INVISIBLE);
                }else{
                    mTv_building.setVisibility(View.VISIBLE);
                    mLin_right.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    @OnClick({R.id.tv_filter,R.id.tv_building,R.id.img_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_filter:
                openMenu();
                break;
            case R.id.tv_building:
                BuildingManageActivity.actionStart(mContext);
                break;
            case R.id.img_search:
                SearchHouseActivity.actionStart(mContext);
                break;

        }
    }
    public void closeMenu() {
        mDrawer_layout.closeDrawer(GravityCompat.END);
    }

    public void openMenu() {
        mDrawer_layout.openDrawer(GravityCompat.END);
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
