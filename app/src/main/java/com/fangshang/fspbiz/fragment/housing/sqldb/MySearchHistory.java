package com.fangshang.fspbiz.fragment.housing.sqldb;

import com.fangshang.fspbiz.App;

import java.util.List;

/**
 * Created by xiong on 2018/1/31/031 12:59
 */

public class MySearchHistory {
    /**
     * 添加数据
     *
     * @param searchHistory
     */
    public static void insertHistory(SearchHistory searchHistory) {
        App.getInstance().getDaoSession().getSearchHistoryDao().insertOrReplace(searchHistory);
    }

    /**
     * 删除数据
     *
     * @param searchHistory
     */
    public static void deleteHistory(SearchHistory searchHistory) {
        App.getInstance().getDaoSession().getSearchHistoryDao().delete(searchHistory);
    }

    /**
     * 查询全部数据
     *
     * @return List<SearchHistory>
     */
    public static List<SearchHistory> queryAll() {
        return App.getInstance().getDaoSession().getSearchHistoryDao().loadAll();
    }

    /**
     * 清空数据
     */
    public static void clearAll() {
        App.getInstance().getDaoSession().getSearchHistoryDao().deleteAll();
    }

}
