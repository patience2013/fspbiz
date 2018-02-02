package com.fangshang.fspbiz.fragment.housing.build;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/23/023 14:20
 */

public class MyReleaseBuildActivity extends BaseBackActivity {
    @BindView(R.id.slidingTab)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.vp_content)
    ViewPager mVp_content;

    private String[] title ={"全部","已通过","审核中","未通过"};
    private List<Fragment> fragments =new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyReleaseBuildActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_myreleasebuild;
    }

    @Override
    protected void initView() {
        setTopTitle("我发布的楼盘");
        MyReleaseBuildListFragment allFragment =new MyReleaseBuildListFragment();
        Bundle bundleall = new Bundle();
        bundleall.putInt("approveSta", 0);
        allFragment.setArguments(bundleall);
        fragments.add(allFragment); //全部

        MyReleaseBuildListFragment PassFragment =new MyReleaseBuildListFragment();
        Bundle bundlepass = new Bundle();
        bundlepass.putInt("approveSta", 1);
        PassFragment.setArguments(bundlepass);
        fragments.add(PassFragment);  //审核通过

        MyReleaseBuildListFragment IngFragment =new MyReleaseBuildListFragment();
        Bundle bundleIng = new Bundle();
        bundleIng.putInt("approveSta", 2);
        IngFragment.setArguments(bundleIng);
        fragments.add(IngFragment);  ////审核中

        MyReleaseBuildListFragment NoPassFragment =new MyReleaseBuildListFragment();
        Bundle bundleNopass = new Bundle();
        bundleNopass.putInt("approveSta",3);
        NoPassFragment.setArguments(bundleNopass);
        fragments.add(NoPassFragment);  //不通过

        mSlidingTab.setViewPager(mVp_content,title,this, (ArrayList<Fragment>) fragments);
        mSlidingTab.showMsg(3, 15);
        mSlidingTab.setMsgMargin(3, 28, 5);
    }


}
