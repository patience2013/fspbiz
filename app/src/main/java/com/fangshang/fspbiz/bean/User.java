package com.fangshang.fspbiz.bean;

import java.io.Serializable;

/**
 * Created by zhucw on 2017/6/21.
 */

public class User implements Serializable {
    private String phone;
    private String username;
    private String nickname;
    private int sta;
    private String avatar;
    private int userType;
    private int bussType;
    private int isAuthentication;
    private String token="";

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSta() {
        return sta;
    }

    public void setSta(int sta) {
        this.sta = sta;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getBussType() {
        return bussType;
    }

    public void setBussType(int bussType) {
        this.bussType = bussType;
    }

    public int getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(int isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
