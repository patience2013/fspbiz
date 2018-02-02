package com.fangshang.fspbiz.base;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by xiong on 2017/12/26/026 12:51
 */

public class BaseBean<E> implements Serializable {
    private static final Gson gson = new Gson();
    private E data;
    private String header;
    private String resultCode;
    private String resultMsg;

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    //    public String getData() {
//        try {
//            if (!"10010".equals(resultCode)&& !"00000".equals(resultCode)) {
//                return null;
//            }
//            return gson.toJson(data);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }

}
