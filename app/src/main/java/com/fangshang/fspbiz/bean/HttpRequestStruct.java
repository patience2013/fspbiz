package com.fangshang.fspbiz.bean;

import java.io.Serializable;

/**
 * Created by xiong on 2018/1/11/011 9:39
 */

public class HttpRequestStruct {
    //请求upyun获取视频截图
    public static class JieTu implements Serializable{
        public JieTu(String source, String point, String save_as) {
            this.source = source;
            this.point = point;
            this.save_as = save_as;
        }

        public String source;//视频的存储地址
        public String point; //时间点
        public String save_as; //截图保存地址
    }
    public static class Signature implements Serializable{
        public Signature(String clientRes, String timestamp, String randomStr) {
            this.clientRes = clientRes;
            this.timestamp = timestamp;
            this.randomStr = randomStr;
        }

        public String clientRes;
        public String timestamp;
        public String randomStr;
    }
    public static class CheckMobile implements Serializable{
        public MsgReq msgReq;
        public String userName;
    }
    public static class GetCode implements Serializable{
        public MsgReq msgReq;
        public String userName;
    }
    public static class Register implements Serializable{
        public MsgReq msgReq;
        public String userName;
        public String password;
        public String verifyCode;
        public int userType;
    }
    public static class Login implements Serializable{
        public MsgReq msgReq;
        public String userName;
        public String password;
    }
    public static class UserIdentity implements Serializable{
        public MsgReqWithToken msgReq;
        public int enterpriseType;
        public String enterpriseName;
        public String enterpriseAddress;
        public String contactPerson;
        public String contactTele;
        public String enterpriseLicense;
        public String bussRegion;
        public String ownerName;
        public String ownerTele;
        public String agentName;
        public String agentTele;
    }
    public static class MsgReq implements Serializable {
        public MsgReq(String clientRes, String signature, String timestamp, String randomStr) {
            this.clientRes = clientRes;
            this.signature = signature;
            this.timestamp = timestamp;
            this.randomStr = randomStr;
        }

        public String clientRes;
        public String signature;
        public String timestamp;
        public String randomStr;
    }
    public static class UserIdentityDetail implements Serializable{
        public MsgReqWithToken msgReq;;
    }
    //请求区域列表
    public static class RegionReq implements Serializable{
        public MsgReqWithToken msgReq;
        public String isOpen; //	0:全部城市 1：系统开通城市
    }
    public static class MsgReqWithToken implements Serializable {
        public MsgReqWithToken(String clientRes,String token, String signature, String timestamp, String randomStr) {
            this.clientRes = clientRes;
            this.token = token;
            this.signature = signature;
            this.timestamp = timestamp;
            this.randomStr = randomStr;
        }

        public String clientRes;
        public String token;
        public String signature;
        public String timestamp;
        public String randomStr;
    }
    //我发布的的楼盘的列表
    public static class MyBuildListReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int pageSize;//每页显示数据量,默认15
        public int pageNo; //当前第几页默认为1
        public int approveSta;//审核状态 1:审核通过 2:审核中 3:审核失败
    }
    //我的楼盘的列表
    public static class MyBuildsListReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int pageSize;//每页显示数据量,默认15
        public int pageNo; //当前第几页默认为1
        public String cityId; //	区域id
        public String estateName;//楼盘名称
    }
    //平台楼盘的列表
    public static class PlatformBuildsListReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int pageSize;//每页显示数据量,默认15
        public int pageNo; //当前第几页默认为1
        public String cityId; //	区域id
        public String estateName;//楼盘名称
    }
    //平台的楼盘详情
    public static class PlatformBuildDetailReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int estateId;//楼盘id
    }
    //我发布的楼盘详情
    public static class MyBuildDetailReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int approveId;//申请id
    }
    //我的房源的列表
    public static class MyHousesListReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int pageSize;//每页显示数据量,默认15
        public int pageNo; //当前第几页默认为1
        public String requestType; //	请求类型 -1:按照房源名称模糊搜索 0：全部 1：已上架 2：未上架 3：审核中 4：未通过 5：已删除
        public int prov;//省
        public int city;//市
        public int district;//区
        public String estateId;//楼盘id
        public String houseType;//房源类型 1:写字楼 2:酒店式soho
        public String rentType	;//租售类型 1:租 2:售
        public String startPrice;//开始价格
        public String endPrice;//结束价格
        public String priceUnit;//单位 1:元/㎡/天 2:元/㎡/月 3:元/月 4:元/㎡ 5:元
        public String startArea	;//	开始面积
        public String endArea;//结束面积
        public String decoration;//装修情况 1:简装修 2:毛培 3:精装修 4：豪华装修
        public String provideRentFree;//提供免租期 1：无 2: 1周 3：半个月 4：20天 5:1个月 6:2个月 7:3个月 8:4个月 9:5个月 10:6个月
        public String houseName	;//	房源名称
    }
    //我的房源详情
    public static class MyHouseDetailReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int houseId;//申请id
    }
    //上下架房源请求的实体类
    public static class HouseUpOrDownReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int houseId;//申请id
        public int opType; //操作类型 1：上架 0：下架
    }
    //删除房源请求的实体类
    public static class HouseDeleteReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int houseId;//申请id
        public int isDeleteCompletely; //是否彻底删除 1:是 0:否
    }
    //删除楼盘请求的实体类
    public static class BuildDeleteReq implements Serializable {
        public MsgReqWithToken msgReq;
        public int estateId;//楼盘id
    }
}
