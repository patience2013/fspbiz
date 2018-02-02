package com.fangshang.fspbiz.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
//    protected RequestManager mImageLoader;
    private boolean mIsDestroy;
    private final String mPackageNameUmeng = this.getClass().getName();
    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());

//        if (!NetUtils.isConnected(this))
//            Toast.makeText(this, "当前没有可用网络！", Toast.LENGTH_LONG).show();

        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());

            initWindow();

            ButterKnife.bind(this);
            initView();
            initData();
        } else {
            finish();
        }

//        //umeng analytics
//        MobclickAgent.setDebugMode(false);
//        MobclickAgent.openActivityDurationTrack(false);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
        }
    }

    protected void replaceFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment);
            transaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract int getContentView();

    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected void initWindow() {
    }

    protected void initView() {
    }

    protected void initData() {
    }

//    public synchronized RequestManager getImageLoader() {
//        if (mImageLoader == null)
//            mImageLoader = Glide.with(this);
//        return mImageLoader;
//    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }

}
