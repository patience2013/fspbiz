package com.fangshang.fspbiz.interfaceUrl;

/**
 * Created by xiong on 2017/12/28/028 15:35
 */

public class InterfaceUrl {
//    public static String BaseUrl ="http://121.40.168.108:3939/";
    public static String BaseUrl ="http://121.40.168.108:9080/";
    public static String IMG_HOST="http://guanlouyi.b0.upaiyun.com";
    //退出登录
    public static String LOGIN_OUT =BaseUrl+"fsp/api/app/user/logout";
    //获取签名
    public static String GET_SIGN =BaseUrl+"fsp/api/common/getSignature";
    //检测用户是否存在
    public static String CHECK_MOBILE =BaseUrl+"fsp/api/app/user/checkUserExist";
    //发送验证码
    public static String GET_CODE =BaseUrl+"fsp/api/app/user/getVerifyCode";
    //发送
    public static String REGISTER =BaseUrl+"fsp/api/app/user/register";
    //登陆
    public static String LOGIN =BaseUrl+"fsp/api/app/user/userLogin";
    //认证
    public static String USERIDENTITY =BaseUrl+"fsp/api/app/user/submitUserIdentity";
    //获取省市区列表
    public static String GET_REGION =BaseUrl+"fsp/api/common/region/getRegion";
    //获取认证详情接口
    public static String GET_USERIDENTITY =BaseUrl+"fsp/api/app/user/getUserIdentityById";
    //更新
    public static String UPDATE =BaseUrl+"fspsaas/api/app/version/query";

    //用户楼盘申请列表接口
    public static String MY_RELEASEBUILD_LIST =BaseUrl+"fsp/api/app/user/estateapprove/list";
    //用户楼盘申请详情接口
    public static String MY_RELEASEBUILD_DETAIL =BaseUrl+"fsp/api/app/user/estateapprove/detail";

    //用户楼盘列表接口
    public static String MY_BUILD_LIST =BaseUrl+"fsp/api/app/user/estate/list";
    //平台楼盘列表接口
    public static String MY_PLATFORM_BUILD_LIST =BaseUrl+"fsp/api/app/platform/estate/list";
    //平台楼盘详情接口
    public static String MY_PLATFORM_BUILD_DETAIL =BaseUrl+"fsp/api/app/platform/estate/detail";
    //用户房源列表
    public static String MY_HOUSE_LIST =BaseUrl+"fsp/api/app/user/house/list";
    //用户房源详情
    public static String MY_HOUSE_DETAIL =BaseUrl+"fsp/api/app/user/house/detail";
    //房源刷新
    public static String REFRESH_HOUSE =BaseUrl+"fsp/api/app/user/house/refresh";
    //房源上下架
    public static String HOUSE_UPORDOWN =BaseUrl+"fsp/api/app/user/house/houseOnSale";
    //房源删除
    public static String DELETE_HOUSE =BaseUrl+"fsp/api/app/user/house/delete";
    //楼盘删除
    public static String DELETE_BUILD =BaseUrl+"fsp/api/app/user/estate/delete";
}
