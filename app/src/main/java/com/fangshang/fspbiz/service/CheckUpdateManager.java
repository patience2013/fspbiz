package com.fangshang.fspbiz.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.activity.UpdateActivity;
import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.DialogCallback;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.util.DialogHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;


public class CheckUpdateManager {
    private static final String TAG = "CheckUpdateManager";

    private ProgressDialog mWaitDialog;
    private Context mContext;
    private boolean mIsShowDialog;
    private RequestPermissions mCaller;

    public CheckUpdateManager(Context context, boolean showWaitingDialog) {
        this.mContext = context;
        mIsShowDialog = showWaitingDialog;
        mWaitDialog = DialogHelper.getProgressDialog(mContext);
        if (mIsShowDialog) {
            mWaitDialog.setMessage("正在检查中...");
            mWaitDialog.setCancelable(false);
            mWaitDialog.setCanceledOnTouchOutside(false);
        }
    }


    public void checkUpdate() {
        if (mIsShowDialog) {
            mWaitDialog.show();
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appType","1" );
        OkGo.<BaseBean<HttpResponseStruct.Versiondata>>post(InterfaceUrl.UPDATE)
                .tag(this)
                .params(paramMap)
                .execute(new DialogCallback<BaseBean<HttpResponseStruct.Versiondata>>((Activity) mContext) {
                    @Override
                    public void onSuccess(Response<BaseBean<HttpResponseStruct.Versiondata>> response) {
                        if("00000".equals(response.body().getResultCode())){
                            if(App.getInstance().getVersionCode()<(int)response.body().getData().version.version){
                                UpdateActivity.show((Activity) mContext,response.body().getData().version);
                            }else {
                                if (mIsShowDialog) {
                                    DialogHelper.getMessageDialog(mContext, "已经是新版本了").show();
                                }
                            }
                            mWaitDialog.dismiss();
                        }
                    }
                });
//        HttpRequest.Get_Version cmd =new HttpRequest.Get_Version("1");
//        HttpNetwork.getInstance().request(cmd, new HttpNetwork.NetResponseCallback() {
//            @Override
//            public void onResponse(HttpRequestPacket request, HttpResponsePacket response) {
//                if (response.code == 0) {
//                    Gson gson = new Gson();
//                    final HttpStruct.Version version = gson.fromJson(response.data, HttpStruct.Version.class);
//                    if (App.getInstance().getVersionCode() < Integer.valueOf(version.value)) {
//                        UpdateActivity.show((Activity) mContext, version);
//                    }else{
//                        if (mIsShowDialog) {
//                            DialogHelper.getMessageDialog(mContext, "已经是新版本了").show();
//                        }
//                    }
//                }
//                if (mIsShowDialog) {
//                    mWaitDialog.dismiss();
//                }
//            }
//        }, new HttpNetwork.NetErrorCallback() {
//            @Override
//            public void onError(HttpRequestPacket request, String errorMsg) throws Exception {
//                if (mIsShowDialog) {
//                    DialogHelper.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
//                }
//            }
//        });
    }

    public void setCaller(RequestPermissions caller) {
        this.mCaller = caller;
    }

    public interface RequestPermissions {
        void call(HttpResponseStruct.Version version);
    }
}
