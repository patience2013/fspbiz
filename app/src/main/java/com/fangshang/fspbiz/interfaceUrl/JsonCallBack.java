package com.fangshang.fspbiz.interfaceUrl;


import com.fangshang.fspbiz.base.BaseBean;
import com.fangshang.fspbiz.util.TsUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiong on 2017/12/28/028 15:49
 */

public abstract class JsonCallBack<T> extends AbsCallback<T> {
    private Type type;
    private Class<T> clazz;
    public JsonCallBack(){

    }
    public JsonCallBack(Type type){
        this.type =type;
    }
    public JsonCallBack(Class<T> clazz){
        this.clazz =clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
//        super.onStart(request);
//        request.params("token","b962758b084845f0a5321e2c09ca3b3813597078080199250765825557158639");
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
//        ResponseBody body =response.body();
//        if(body==null)return  null;
//
//        T data =null;
//        Gson gson =new Gson();
//        JsonReader jsonReader =new JsonReader(body.charStream());
//        if(type!=null){
//            data =gson.fromJson(jsonReader,type);
//        }else if(clazz!=null){
//            data =gson.fromJson(jsonReader,clazz);
//        }else{
//            Type genType =getClass().getGenericSuperclass();
//            Type type =((ParameterizedType)genType).getActualTypeArguments()[0];
//            data =gson.fromJson(jsonReader,type);
//        }
//        return data;


        Type genType =getClass().getGenericSuperclass();
        Type[] parmas =((ParameterizedType)genType).getActualTypeArguments();
        //这里得到第二层泛型的所有类型
        Type type =parmas[0];

        if(!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        //这里得到第二层数据的真实类型
        Type rawType =((ParameterizedType)type).getRawType();
        //这里得到第二层数据的泛型真实类型
        Type typeArgument =((ParameterizedType)type).getActualTypeArguments()[0];

        ResponseBody body =response.body();
        if(body==null)return  null;

        Gson gson =new Gson();
        JsonReader jsonReader =new JsonReader(body.charStream());
        if(rawType != BaseBean.class){
            T data =gson.fromJson(jsonReader,type);
            response.close();
            return data;
        }else {
            if(typeArgument ==Void.class){
                //无数据类型，表示有data
//                Simpl
                BaseBean baseBean =gson.fromJson(jsonReader,BaseBean.class);
                response.close();
                return (T) baseBean;
            }else {
                //有数据类型
                BaseBean baseBean =gson.fromJson(jsonReader,type);
                response.close();
                String code =baseBean.getResultCode();
                //这里的0是以下意思
                if(code.equals("00000")){
                    return (T)baseBean;
                }else if(code.equals("10000")){
                    throw  new IllegalStateException("用户登录失败");
                }else{
                    return (T)baseBean;
//                    throw new IllegalStateException("错误代码"+code+"错误信息"+baseBean.getResultMsg());
                }
            }
        }
    }
}
