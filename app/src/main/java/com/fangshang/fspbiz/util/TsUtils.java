package com.fangshang.fspbiz.util;

import com.blankj.utilcode.util.ToastUtils;

public class TsUtils {
    public static void show(String res) {
        ToastUtils.showShort(res);
    }

    public static void errey_show() {
        ToastUtils.showShort("数据异常!~~~~");
    }
}