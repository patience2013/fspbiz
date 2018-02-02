package com.fangshang.fspbiz.util;

import android.content.Context;

import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.bean.HttpRequestStruct;
import com.fangshang.fspbiz.bean.HttpResponseStruct;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.fangshang.fspbiz.interfaceUrl.JsonCallBack;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

/**
 * Created by xiong on 2018/1/10/010 19:40
 */

public class HttpUtil {
    private static String signatureString;
        public static String getSignature(Context context, final String date,final String number, final NetListener netListener){
        final HttpRequestStruct.Signature signature =new HttpRequestStruct.Signature("android",date+"",number);
            final Gson gson =new Gson();
            String json =gson.toJson(signature);
        OkGo.<BaseBean<HttpResponseStruct.SigntureString>>post(InterfaceUrl.GET_SIGN)
                .tag(context)
                .upJson(json)
                .execute(new JsonCallBack<BaseBean<HttpResponseStruct.SigntureString>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<HttpResponseStruct.SigntureString>> response) {
                        if("00000".equals(response.body().getResultCode())){
                            signatureString =response.body().getData().signature;
                            Logger.d(response.body().getData().signature);
                            netListener.success(signatureString);
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<HttpResponseStruct.SigntureString>> response) {
                        Logger.d(response.body());
                        super.onError(response);
                    }
                });
        return signatureString;
    }
    public interface NetListener {
        void success(String sign);
    }
//    public void getSign(){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Call<String> call =OkGo.<String>get(InterfaceUrl.GET_SIGN)
//                            .tag(this)
//                }
//            });
//    }
    public static String getRandom(){
        int numcode = (int) ((Math.random() * 9 + 1) * 100000);
        return numcode+"";
    }
}
