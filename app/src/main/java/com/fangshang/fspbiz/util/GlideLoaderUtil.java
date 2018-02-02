package com.fangshang.fspbiz.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fangshang.fspbiz.App;
import com.fangshang.fspbiz.interfaceUrl.InterfaceUrl;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by xiong on 2018/1/15/015 19:38
 */

public class GlideLoaderUtil {
    public static void display(Context context,ImageView imageView,String path){
        Glide.with(context).load(InterfaceUrl.IMG_HOST+path).into(imageView);
    }
}
