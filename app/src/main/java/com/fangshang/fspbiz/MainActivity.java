package com.fangshang.fspbiz;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.fangshang.fspbiz.activity.login.CertificationActivity;
import com.fangshang.fspbiz.base.BaseActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.fragment.housing.HousingFragment;
import com.fangshang.fspbiz.fragment.main.MainFragment;
import com.fangshang.fspbiz.fragment.me.MeFragment;
import com.fangshang.fspbiz.fragment.NormalFragment;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.service.CheckUpdateManager;
import com.fangshang.fspbiz.service.DownloadService;
import com.fangshang.fspbiz.util.DialogHelper;
import com.fangshang.fspbiz.util.HttpUtil;
import com.fangshang.fspbiz.util.Setting;
import com.fangshang.fspbiz.weight.NoSlideViewPager;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,CheckUpdateManager.RequestPermissions{
    @BindView(R.id.bnve)
    BottomNavigationViewEx bottomNavigationViewEx;
    @BindView(R.id.vp)
    NoSlideViewPager viewPager;
    Gson gson =new Gson();

    private HttpResponseStruct.Version mVersion;
    private static final int RC_EXTERNAL_STORAGE = 0x04;//存储权限
    // collections
    private List<Fragment> fragments;// used for ViewPager adapter
    private VpAdapter adapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        fragments = new ArrayList<>();

        // create music fragment and add it
        MainFragment musicFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "首页");
        musicFragment.setArguments(bundle);

        // create backup fragment and add it
        HousingFragment backupFragment = new HousingFragment();
        bundle = new Bundle();
        bundle.putString("title", "房源");
        backupFragment.setArguments(bundle);

        // create friends fragment and add it
        NormalFragment friendsFragment = new NormalFragment();
        bundle = new Bundle();
        bundle.putString("title", "招商");
        friendsFragment.setArguments(bundle);

        NormalFragment loveFragment = new NormalFragment();
        bundle = new Bundle();
        bundle.putString("title", "消息");
        loveFragment.setArguments(bundle);

        MeFragment meFragment = new MeFragment();
        bundle = new Bundle();
        bundle.putString("title", "我的");
        meFragment.setArguments(bundle);
        // add to fragments for adapter
        fragments.add(musicFragment);
        fragments.add(backupFragment);
        fragments.add(friendsFragment);
        fragments.add(loveFragment);
        fragments.add(meFragment);

        // set adapter
        adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        // binding with ViewPager
        bottomNavigationViewEx.setupWithViewPager(viewPager);
//        addBadgeAt(0,9);
    }

    @Override
    protected void initData() {
        checkUpdate();
        if(AccountHelper.getUser().getUserType()!=3){
            checkUserIdentity();
        }
    }
    private void checkUpdate() {
        if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        CheckUpdateManager manager = new CheckUpdateManager(this, false);
        manager.setCaller(this);
        manager.checkUpdate();
    }
    private void checkUserIdentity(){
            final long date = new Date().getTime();
            final String number = HttpUtil.getRandom();
            HttpUtil.getSignature(this, date + "", number, new HttpUtil.NetListener() {
                @Override
                public void success(final String sign) {
                    HttpRequestStruct.UserIdentityDetail userIdentityDetail =new HttpRequestStruct.UserIdentityDetail();
                    HttpRequestStruct.MsgReqWithToken msgReqWithToken =new HttpRequestStruct.MsgReqWithToken("android", AccountHelper.getUser().getToken(),sign,date+"",number);
                    userIdentityDetail.msgReq =msgReqWithToken;
                    String json = gson.toJson(userIdentityDetail);
                    OkGo.<BaseBean<HttpResponseStruct.UserIdentityData>>post(InterfaceUrl.GET_USERIDENTITY)
                            .tag(this)
                            .upJson(json)
                            .execute(new DialogCallback<BaseBean<HttpResponseStruct.UserIdentityData>>(MainActivity.this) {
                                @Override
                                public void onSuccess(Response<BaseBean<HttpResponseStruct.UserIdentityData>> response) {
                                    Logger.d("    token    "+AccountHelper.getUser().getToken()+"    date    "+date+"    number    "+number+"    sign    "+sign);
                                    Logger.d("code"+response.body().getResultCode()+"msg"+response.body().getResultMsg());
                                    if("00000".equals(response.body().getResultCode())){
                                        if(response.body().getData().userIdentityDetail.isAuthentication==0){
                                            DialogHelper.getConfirmDialog(MainActivity.this, "", "q", "确定", "", false, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    CertificationActivity.actionStart(MainActivity.this,AccountHelper.getUser().getBussType(),null);
                                                }
                                            }).show();
                                        }else if(response.body().getData().userIdentityDetail.isAuthentication==1){
//                                            mTv_certification.setText("审核通过");
//                                            mImg_status.setImageResource(R.drawable.certification_4);

                                        }else if(response.body().getData().userIdentityDetail.isAuthentication==2){
//                                            mTv_certification.setText("审核中");
//                                            mImg_status.setImageResource(R.drawable.certification_3);
                                        }else if(response.body().getData().userIdentityDetail.isAuthentication==3){
//                                            mTv_certification.setText("审核失败");
//                                            mImg_status.setImageResource(R.drawable.certification_2);
                                        }
                                    }
                                }
                            });
                }
            });
    }
    @Override
    public void call(HttpResponseStruct.Version version) {
        this.mVersion = version;
        requestExternalStorage();
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        public VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
    private Badge addBadgeAt(int position, int number) {
        // add badge
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bottomNavigationViewEx.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Toast.makeText(MainActivity.this, R.string.tips_badge_removed, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * 再按一次退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @AfterPermissionGranted(RC_EXTERNAL_STORAGE)
    public void requestExternalStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DownloadService.startService(this, mVersion.url);
        } else {
            EasyPermissions.requestPermissions(this, "", RC_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        for (String perm : perms) {
            if (perm.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                DialogHelper.getConfirmDialog(this, "温馨提示", "需要开启房算盘对您手机的存储权限才能下载安装，是否现在开启", "去开启", "取消", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                    }
                }, null).show();

            } else {
                Setting.updateLocationPermission(getApplicationContext(), false);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
