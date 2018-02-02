package com.fangshang.fspbiz.fragment.housing.sqldb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SearchHistory {
    @Id(autoincrement = true) //自增主键
    private Long id;
    @Unique // 搜索记录(唯一)
    private String name;
    @Generated(hash = 822577210)
    public SearchHistory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1905904755)
    public SearchHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}