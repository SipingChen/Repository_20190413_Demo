package cn.itcast.travel.service;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;

import java.lang.reflect.InvocationTargetException;

public interface FavoriteService {

    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);

    /**
     * 查询用户收藏
     * @param o
     * @param uid
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageBean<Favorite> pageQuery(int uid, int currentPage, int pageSize) throws InvocationTargetException, IllegalAccessException;

    PageBean<Favorite> pageQueryRank(int currentPage, int pageSize, String rname, String maxmoney, String minmoney) throws InvocationTargetException, IllegalAccessException;
}
