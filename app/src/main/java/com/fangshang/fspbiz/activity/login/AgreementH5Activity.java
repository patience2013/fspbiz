package com.fangshang.fspbiz.activity.login;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.R;
import com.fangshang.fspbiz.base.BaseBackActivity;

import butterknife.BindView;

/**
 * Created by xiong on 2018/1/31/031 19:28
 */

public class AgreementH5Activity extends BaseBackActivity {
    @BindView(R.id.web_agreement)
    WebView mWeb_agreement;
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AgreementH5Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agreementh5;
    }

    @Override
    protected void initView() {
        super.initView();
        setTopTitle("注册协议");
        WebSettings mWebSettings = mWeb_agreement.getSettings();
        mWebSettings.setJavaScriptEnabled(true);//设置支持Javascript
        mWebSettings.setUseWideViewPort(true);//设置自适应屏幕
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
//        mWeb_agreement.addJavascriptInterface(new AddBuildActivity.JsInterface(this), "android");
        //添加客户端支持
        mWeb_agreement.setWebChromeClient(new WebChromeClient());

        mWeb_agreement.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                sendInfoToJs();
            }
        });
//        progressBarSum.setMax(100);
        mWeb_agreement.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                progressBarSum.setProgress(newProgress);
                if (newProgress >= 100) {
//                    progressBarSum.setVisibility(View.GONE);
                }
            }
        });
        mWeb_agreement.loadUrl(App.H5_HOST+"protocol.html ");
    }
}
