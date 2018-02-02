package com.fangshang.fspbiz.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;


import com.fangshang.fspbiz.App;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liudong on 2017/6/8.
 */

public class ZhuanHuanUtil {
    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(App.getInstance(), id);
    }

    @ColorInt
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(App.getInstance(), id);
    }

    public static String Time2nian(long l) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(l);
    }

    public static String Time2ShiFen(long l) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(l);
    }

    public static String Time2nian2(long l) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(l);
    }

    public static String Time2fen(long l) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(l);
    }


    //10分钟时间戳
    public static long getMinute(long min) {
        return min * 60 * 1000;
    }



    public static String setNumOr(double Birthday) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(Birthday);
    }

    public static String setNumOr0(double Birthday) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(Birthday);
    }

    public static String setNumOr00(float Birthday) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Birthday);
    }

    public static String setPhoneXX(String phone) {
        String substring = phone.substring(3, 7);
        return phone.replace(substring, "****");
    }
    /**
     * 获取年份
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getYearDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM/dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /**
     * 获取当月
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getTimes(String time) {
        Long times=new Long(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(times);
        return dateString;
    }
    /**
     * 获取当月
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getDayDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
