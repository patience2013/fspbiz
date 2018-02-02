package com.fangshang.fspbiz.bean;

import java.io.Serializable;

/**
 * Created by xiong on 2018/1/29/029 16:05
 */

public class RentBean implements Serializable{
    public RentBean(String title, boolean isSelect) {
        this.title = title;
        this.isSelect = isSelect;
    }

    public String title;
    public boolean isSelect;
}
