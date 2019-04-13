package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface FavoriteDao {

    /**
     * 根据rid和uid查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid 查询收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param i
     * @param uid
     */
    void add(int i, int uid);

    int findTotalCount(int uid);

    List<Favorite> findByPage(int uid, int start, int pageSize) throws InvocationTargetException, IllegalAccessException;

    int findTotalCountRank(String rname, String maxmoney, String minmoney);

    List<Favorite> findByPageRank(int start, int pageSize, String rname, String maxmoney, String minmoney) throws InvocationTargetException, IllegalAccessException;
}
