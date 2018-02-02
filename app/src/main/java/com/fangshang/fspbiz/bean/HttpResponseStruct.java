package com.fangshang.fspbiz.bean;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.fangshang.fspbiz.fragment.me.activity.SetActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiong on 2017/12/28/028 14:59
 */

public class HttpResponseStruct {

    public class SigntureString implements Serializable{
        public String signature;
    }
    public class CheckBoolean implements Serializable{
        public boolean userExist;
    }
    public static class LoginData implements Serializable {
        public User user;
    }
    public class Versiondata implements Serializable {
        public Version version;
    }
    public class Version implements Serializable {
        public String appType;
        public double version;//版本号
        public String url;//更新地址
        public String upgradeTxt;//更新日志
    }
    public class UserIdentity implements Serializable{
        public boolean flag;
    }
    public class HouseSourceData implements Serializable {
        public List<BuildingListInfo> list;
    }

    public class BuildingListInfo implements Serializable {
        /**
         * id : 81
         * createTime : 1504855970000
         * updateTime : 1504855970000
         * dataSta : 1
         * projectId : 18
         * name : 天猫1号楼
         * customerLift : 12
         * goodsLift : 6
         * floors : 0
         * type : 1
         * area : 50000
         * templateId : null
         */

        public int id;
        public long createTime;
        public long updateTime;
        public String dataSta;
        public int projectId;
        public String name;
        public String customerLift;
        public String goodsLift;
        public String floors;
        public String type;
        public String area;
        public String templateId;
        public List<BuildingFloorInfo> floorlist;
    }

    public class BuildingFloorInfo implements Serializable {

        public int id;
        public long createTime;
        public long updateTime;
        public String dataSta;
        public String buildingId;
        public String name;
        public String floorHigh;
        public String projectId;
        public String floorImg;
        public String floorArea;
        public List<HouseListBean> houselist;
    }

    public class HouseListBean implements Serializable {

        public int id;
        public String createTime;
        public String updateTime;
        public String dataSta;
        public String floorId;
        public String sysHouseName;
        public String houseName;
        public String alias;
        public String acreage;
        public String innerAcreage;
        public String houseRate;
        public String orientation;
        public String decoration;
        public String houseProp;
        public String purpose;
        public String type;
        public String houseMerge;
        public String houseSta;
        public String contractId;
        public String projectId;
        public String mergeHouseId;
        public String checkinId;
        public String mergeHouseName;
        public String isPub;
        public String pubMoney;
        public String pubUtil;
        public String pubType;
    }
    public static class Housebean implements Serializable {
        public Housebean() {
        }

        public Housebean(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String id;
        public String title;
    }
    public static class HouseIdTitle implements Serializable {
        public String buildingId;
        public String floorId;
        public String houseId;

    }

    public static class AreaData implements Serializable{
        public List<Province> regions;
    }
    //省份
    public static class Province implements Serializable,IPickerViewData {
        public int id;
        public String name;
        public int isOpen;
        public List<City> childRegions;

        @Override
        public String getPickerViewText() {
            return name;
        }
    }

    //市区
    public static class City implements Serializable {
        public int id;
        public String name;
        public int isOpen;
        public List<Area> childRegions;
    }

    //区
    public static class Area implements Serializable {
        public int id;
        public String name;
        public int isOpen;
        public boolean isCheck;
    }
    public static class UserIdentityData implements Serializable{
        public UserIdentityDetail userIdentityDetail;
    }
    public static class  UserIdentityDetail implements Serializable{
        public String createTime;//提交时间
        public int userType; //	用户类型 1:企业 2:经纪人 3：个人
        public String enterpriseType; //企业类型 1：企业招商 2:中介公司 3：经纪人
        public String enterpriseName;//企业名称
        public String enterpriseAddress;//企业地址
        public String contactPerson;//对接人
        public String contactTele;//	对接人手机
        public String enterpriseLicense; //企业营业执照
        public String bussRegion;   //业务区域
        public String ownerName;    //业主姓名
        public String ownerTele;    //业主手机
        public String agentName;    //	经纪人姓名
        public String agentTele;    //经纪人手机
        public int isAuthentication;    //是否认证 0:未审核 1:审核通过 2:审核中 3:审核失败
        public String authTime;     //审批时间
        public String approveIssue; //审批意见
    }
    //平台的楼盘数据
    public static class BuildPlatformListData implements Serializable {
        public BuildListPage estates;
    }

    //用户发布的楼盘数据
    public static class BuildListData implements Serializable {
        public BuildListPage page;
    }

    public static class BuildListPage implements Serializable {
        public List<BuildSimpleDetail> dataList;
        public int pageNo;
        public int totalPage;
    }
    //用户楼盘数据
    public static class UserBuildListData implements Serializable {
        public BuildListPage userEstates;
    }
    public static class BuildSimpleDetail implements Serializable {
        public int id;  //申请编号
        public String pic;//列表图片
        public String approveEstatePicture; //缩略图
        public String estateName; //楼盘名称
        public String startArea; //开始范围
        public String endArea; //结束范围
        public String startPrice; //开始价格
        public String endPrice; //结束价格
        public int unit; //1: 元/m²/天起 2: 元/m²/月起 3: 元/月起4: 元/m²/天5: 元/m²/月6: 元/月7: 元/m²8: 元9: 元/工位/月10: 元/间/月
        public int isNegotiable;//是否面议 1：是 0：否
        public int approveSta; //审核状态 0:未审核 1:审核通过 2:审核中 3:审核失败
        public boolean isSelect;//选中状态 做筛选用的
    }
    //平台楼盘
    public static class PlantformBuildData implements Serializable{
        public PlantformBuildDetail detail;
    }
    //平台楼盘申请详情页
    public static class PlantformBuildDetail implements Serializable{
        public Approve estate;
        public List<Attachment> attachments;
    }

    //楼盘
    public static class BuildData implements Serializable{
        public BuildDetail detail;
    }
    //楼盘申请详情页
    public static class BuildDetail implements Serializable{
        public Approve approve;
        public List<Attachment> attachments;
    }
    public static class Approve implements Serializable {
        public int id; //楼盘id
        public long createTime; //创建时间
        public String updateTime;//更改时间
        public int userId;  //用户id
        public int operateType;//待定
        public String estateId;//楼盘id
        public int approveSta;//审核状态
        public String approveId; //审核id
        public String approveTime;//审核时间
        public String approveIssue;//审核意见
        public String estateName; //楼盘名称
        public int estateType; //楼盘类型: 1:写字楼 2:联合办公 3:酒店式soho 4:厂房
        public int rentSaleType; //	租售类型: 1:租 2:售
        public String startPrice; //价格区域的开始价格
        public String endPrice;
        public int unit; //价格的单位 1: 元/m²/天起 2: 元/m²/月起 3: 元/月起4: 元/m²/天5: 元/m²/月6: 元/月7: 元/m²8: 元9: 元/工位/月10: 元/间/月
        public String isNegotiable; //是否面议 1：是 0:否
        public long prov; //省id
        public long city;  //市id
        public long district; //区id
        public String circleId;//商圈id
        public String contact; //联系人
        public String contactCellphone; //联系人手机
        public String contactTel; //联系人固话
        public String constructionArea; //建筑面积
        public String startArea;    //范围面积开始值
        public String endArea;  ////范围面积结束值
        public String estateAddress; //楼盘地址
        public String longitude; //经度
        public String latitude; //纬度
        public int parkAmount; //停车位数量
        public String parkFee; //停车费
        public String propertyFee; //物业费
        public String propertyUnit; //物业费单价 1:元/㎡/月 2:元/月
        public String energyFee; //能耗费
        public String energyUnit; //能耗费单价 1:元/㎡/月 2:元/月
        public String propertyCompany; //物业公司
        public String floor; //所在楼层
        public String sapceArea; //空间面积
        public String workStation; //总工位
        public String rentType; //租赁方式 例如：5_300,6_500
        public String parkRequire; //入驻要求
        public String servicePlatform; //服务平台：1投融资 2注册年检 3 财务支持 4人力资源 5创业辅导 6政策顾问
        public String spaceService; //空间服务 1会议室 2书吧 3咖啡厅 4茶水区 5复印打印 6前台接待 7办公宽带 8传真 9WIFI 10洽谈室 11多功能厅 12 远程视频
        public String buildYear; //建筑年代
        public String floorHeight; //层高
        public String floorAmount; //层数
        public String lift;    //电梯
        public String buildStructure; //建筑结构
        public String factoryBulidingArea; //建筑面积
        public String floorBearing;  //	楼层承重量
        public String powerCapacity; //配电容量
        public String carInOut;  //车辆进出
        public String goodsLiftNum; //货梯数量
        public String minRentYear; //最短租期
        public String fitCompany; //适合企业
        public String label;  //标签
        public int dataSta; //
        public String estateDescription;//楼盘描叙
        public String zoneName; //所在地
        public String businessCircleName;
    }
    public static class Attachment implements Serializable{
        public String coverPic; //封面图片
        public String thumb; //缩略图
        public String filePath; //文件路径
        public int fileType;    //1:图片 2：视屏
        public int position;    //视屏 0:其他 1:楼外景 2:大厅 3:其他  图片: 0:其他 1远外景 2:近外景 3:大门 4:大堂 5:景观 6:电梯厅 7:公共区
    }
    //用户房源数据
    public static class HouseListData implements Serializable {
        public HouseListPage page;
    }
    public static class HouseListPage implements Serializable {
        public List<HouseSimpleDetail> dataList;
        public int pageNo;
        public int totalPage;
    }
    public static class HouseSimpleDetail implements Serializable {
        public int id;  //房源id
        public int estateId;//楼盘id
        public String estateName;//楼盘名
        public String building;//楼栋名称
        public String floor; //楼层名称
        public String houeNo; //房源编号
        public String area; //面积
        public String price; //价格
        public int unit; //1: 元/m²/天起 2: 元/m²/月起 3: 元/月起4: 元/m²/天5: 元/m²/月6: 元/月7: 元/m²8: 元9: 元/工位/月10: 元/间/月
        public String decoration;// 装修情况 1:简装修 2:毛培 3:精装修 4：豪华装修
        public int approveSta; //审核状态 0:未审核 1:审核通过 2:审核中 3:审核失败
        public int isOnSale;//是否上架
        public String refreshTime;//刷新时间
        public String pushAmount;//推送次数
        public String pic; //图片地址
    }
    //房源
    public static class HouseData implements Serializable{
        public HouseDetail detail;
    }
    //楼盘申请详情页
    public static class HouseDetail implements Serializable{
        public House house;
        public List<Attachment> attachments;
    }
    public static class House implements Serializable{
        public long createTime; //创建时间
        public int estateId;  //楼盘id
        public String estateName; //楼盘名称
        public int houseType; //房源类型 1:写字楼 2:酒店式soho
        public String building; //楼栋
        public int floor; //层数
        public int houeNo;  //房源号
        public double area; //面积
        public int rentType; //租售类型 1:租  2:售
        public double price; //价格
        public int unit;    //单位 1:元/㎡/天 2:元/㎡/月 3:元/月  4:元/㎡ 5:元
        public int payMonth;  //付多少月
        public int depositMonth;  //押多少月
        public String contactPerson;  //联系人
        public String contactCellphone; //联系电话
        public int agentCooperate;    //是否和经纪人合作 1：是  0：否
        public String provideRentFree;  //提供免租期  1：无   2: 1周 3：半个月 4：20天  5:1个月   6:2个月   7:3个月   8:4个月  9:5个月   10:6个月
        public String decoration;    //装修情况 1:简装修 2:毛培 3:精装修  4：豪华装修
        public String orientation;   //朝向 1东2南3西4北5东南6西南7东北8西北 9南北 10东西
        public String floorHeight;   //层高
        public String registCompany;    //是否可以注册公司  1：是  0：否
        public String houseDescription; //房源介绍
        public String housePurpose;     //房源作用 1：商住两用 2:办公 3：居住
        public String basicCfg;         //基本配置 1:空调 2:床 3:书桌/办公桌 4:衣柜 5:阳台 6:卫生间 多个逗号隔开
        public String extraCfg;           //额外配置 1:wifi 2:宽带 3:热水器 4:洗衣机 5:冰箱 6:燃气灶 7:油烟机 8:电视 9:微波炉
        public int approveSta;              //审核状态 0:未审核 1:审核通过 2:审核中 3:审核失败
        public String approveTime;          //审核时间
        public String approveIssue;         //审核意见
        public int isOnSale;            //是否上架  1:是  0:否
        public String refreshTime;      //刷新时间
        public int pushAmount;          //推送次数
    }
    public static class DelHouseData implements Serializable{
        public boolean flag;//操作结果 成功:true 失败 false
    }
}
