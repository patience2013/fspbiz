package com.fangshang.fspbiz.fragment.housing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xiong on 2018/1/8/008 13:52
 */

public class MyHouseFragment extends BaseFragment {
    @BindView(R.id.slidingTab)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.vp_content)
    ViewPager mVp_content;

    private String[] title ={"全部","已上架","未上架","待审核","未通过","已删除"};
    private List<Fragment> fragments =new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myhouse;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //全部
        HouseListFragment allFragment =new HouseListFragment();
        Bundle bundleall1 = new Bundle();
        bundleall1.putInt("requestType", 0);//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        allFragment.setArguments(bundleall1);
        fragments.add(allFragment);
        //已上架
        HouseListFragment addedFragment =new HouseListFragment();
        Bundle bundleadded = new Bundle();
        bundleadded.putInt("requestType", 1);//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        addedFragment.setArguments(bundleadded);
        fragments.add(addedFragment);
        //未上架
        HouseListFragment noaddFragment =new HouseListFragment();
        Bundle bundlenoadd = new Bundle();
        bundlenoadd.putInt("requestType", 2);//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        noaddFragment.setArguments(bundlenoadd);
        fragments.add(noaddFragment);
        //审核中
        HouseListFragment auditingFragment =new HouseListFragment();
        Bundle bundleauditing = new Bundle();
        bundleauditing.putInt("requestType", 3);//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        auditingFragment.setArguments(bundleauditing);
        fragments.add(auditingFragment);
        //未通过
        HouseListFragment nopassFragment =new HouseListFragment();
        Bundle bundlenopass = new Bundle();
        bundlenopass.putInt("requestType",4 );//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        nopassFragment.setArguments(bundlenopass);
        fragments.add(nopassFragment);
        //已删除
        HouseListFragment delFragment =new HouseListFragment();
        Bundle bundledel = new Bundle();
        bundledel.putInt("requestType",5 );//请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        delFragment.setArguments(bundledel);
        fragments.add(delFragment);
        //
        mSlidingTab.setViewPager(mVp_content,title,(FragmentActivity) mContext, (ArrayList<Fragment>) fragments);
//        mSlidingTab.showMsg(3, 15);
//        mSlidingTab.setMsgMargin(3, 0, 5);
    }
    @OnClick(R.id.img_addhouse)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_addhouse:
                AddHouseActivity.actionStart(mContext,"","","");
                break;
        }
    }
}
