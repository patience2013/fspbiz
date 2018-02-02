package com.fangshang.fspbiz.interfaceUrl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.lzy.okgo.request.base.Request;

/**
 * Created by xiong on 2017/12/28/028 16:24
 */

public abstract class DialogCallback<T> extends JsonCallBack<T>{
    private ProgressDialog dialog;
    private void initDialog(Activity activity){
        dialog =new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求中...");
    }
    public DialogCallback(Activity activity){
        super();
        initDialog(activity);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if(dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
