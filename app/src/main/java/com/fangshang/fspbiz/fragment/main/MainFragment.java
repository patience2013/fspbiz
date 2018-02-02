package com.fangshang.fspbiz.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.activity.LoginActivity;
import com.fangshang.fspbiz.activity.login.AgreementH5Activity;
import com.fangshang.fspbiz.base.BaseFragment;
import com.fangshang.fspbiz.fragment.housing.AddBuildActivity;
import com.fangshang.fspbiz.fragment.housing.AddHouseActivity;
import com.fangshang.fspbiz.service.AccountHelper;
import com.fangshang.fspbiz.util.Config;
import com.fangshang.fspbiz.util.TsUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import butterknife.BindView;

/**
 * Created by xiong on 2017/12/16/016 17:20
 */

public class MainFragment extends BaseFragment{
    @BindView(R.id.web_main)
    WebView mWeb_main;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mainh5;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        WebSettings mWebSettings = mWeb_main.getSettings();
        mWebSettings.setJavaScriptEnabled(true);//设置支持Javascript
        mWebSettings.setUseWideViewPort(true);//设置自适应屏幕
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWeb_main.addJavascriptInterface(new JsInterface(mContext), "android");
        //添加客户端支持
        mWeb_main.setWebChromeClient(new WebChromeClient());

        mWeb_main.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                sendInfoToJs();
            }
        });
//        progressBarSum.setMax(100);
        mWeb_main.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                progressBarSum.setProgress(newProgress);
                if (newProgress >= 100) {
//                    progressBarSum.setVisibility(View.GONE);
                }
            }
        });
        mWeb_main.loadUrl(App.H5_HOST+"index.html ");
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class JsInterface {
        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public String getToken() {
            if (!"".equals(AccountHelper.getUser().getToken())) {
                return AccountHelper.getUser().getToken();
            }
            return "";
        }

        @JavascriptInterface
        public void goBuild() {
            AddBuildActivity.actionStart(mContext,"");
        }
        @JavascriptInterface
        public void goHouse() {
            AddHouseActivity.actionStart(mContext,"","","");
        }
        //登陆验证失败的
        @JavascriptInterface
        public void login() {
            TsUtils.show("登陆信息已过期，请重新登陆");
            Config.getInstance().set(Config.PASSWORD, "");
            Config.getInstance().set(Config.USERNAME, "");
            Config.getInstance().set(Config.TOKEN, "");
            AccountHelper.clearUserCache();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
//        LoginActivity.actionStart(mContext);
        }
    }
}
