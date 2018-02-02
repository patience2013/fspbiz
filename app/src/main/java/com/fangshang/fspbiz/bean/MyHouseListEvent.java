package com.fangshang.fspbiz.bean;

import java.io.Serializable;

/**
 * Created by xiong on 2018/1/30/030 18:44
 */

public class MyHouseListEvent implements Serializable{
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

        @Override
        public String toString() {
                return "MyHouseListEvent{" +
                        "pageSize=" + pageSize +
                        ", pageNo=" + pageNo +
                        ", requestType='" + requestType + '\'' +
                        ", prov=" + prov +
                        ", city=" + city +
                        ", district=" + district +
                        ", estateId='" + estateId + '\'' +
                        ", houseType='" + houseType + '\'' +
                        ", rentType='" + rentType + '\'' +
                        ", startPrice='" + startPrice + '\'' +
                        ", endPrice='" + endPrice + '\'' +
                        ", priceUnit='" + priceUnit + '\'' +
                        ", startArea='" + startArea + '\'' +
                        ", endArea='" + endArea + '\'' +
                        ", decoration='" + decoration + '\'' +
                        ", provideRentFree='" + provideRentFree + '\'' +
                        ", houseName='" + houseName + '\'' +
                        '}';
        }
}
