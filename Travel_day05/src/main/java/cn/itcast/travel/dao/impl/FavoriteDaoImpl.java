package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCountRank(String rname, String maxmoney, String minmoney) {
//        String sql = "SELECT COUNT(*) FROM tab_favorite";
//        return template.queryForObject(sql,Integer.class);
//        SELECT COUNT(*) FROM tab_route tr,tab_favorite tf WHERE tf.`rid`=tr.`rid` AND tr.`rname` LIKE '%春节%' AND tr.`price` > 2000 AND tr.`price`;

        //1.定义sql模板
        String sql = "SELECT COUNT(*) FROM tab_route tr,tab_favorite tf WHERE tf.rid=tr.rid ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }

        if (maxmoney != null && maxmoney.length() > 0 && !"null".equals(maxmoney)) {
            Double maxM = Double.parseDouble(maxmoney);
            sb.append(" and tr.price < ? ");
            params.add(maxM);
        }

        if (minmoney != null && minmoney.length() > 0 && !"null".equals(minmoney)) {
            Double minM = Double.parseDouble(minmoney);
            sb.append(" and tr.price > ? ");
            params.add(minM);
        }

        sql = sb.toString();
        System.out.println("count: "+sql);
        return template.queryForObject(sql, Integer.class, params.toArray());

    }

    @Override
    public List<Favorite> findByPageRank(int start, int pageSize, String rname, String maxmoney, String minmoney) throws InvocationTargetException, IllegalAccessException {
        //1.定义sql模板
        String sql = "SELECT * FROM tab_route tr,tab_favorite tf WHERE tf.rid=tr.rid ";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }

        if (maxmoney != null && maxmoney.length() > 0 && !"null".equals(maxmoney)) {
            Double maxM = Double.parseDouble(maxmoney);
            sb.append(" and tr.price < ? ");
            params.add(maxM);
        }

        if (minmoney != null && minmoney.length() > 0 && !"null".equals(minmoney)) {
            Double minM = Double.parseDouble(minmoney);
            sb.append(" and tr.price > ? ");
            params.add(minM);
        }
        sb.append("ORDER BY tr.count DESC limit ? ,?");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        System.out.println("Select: "+sql);
        List<Map<String, Object>> maps = template.queryForList(sql, params.toArray());
        ArrayList<Favorite> favorites = new ArrayList<>();
        for (Map<String, Object> mm : maps) {
            Route rr = new Route();
            BeanUtils.populate(rr,mm);
            Favorite ff = new Favorite();
            BeanUtils.populate(ff,mm);
            ff.setRoute(rr);
            favorites.add(ff);
        }
        return favorites;
    }

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
           // e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";

        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql, rid, new Date(), uid);
    }

    /**
     * 查询用户的收藏记录条数
     * @param uid
     * @return
     */
    @Override
    public int findTotalCount(int uid) {
        String sql = " select count(*) from tab_favorite where uid = ? ";
        return template.queryForObject(sql,Integer.class,uid);
    }

    /**
     * 查询用户收藏的记录
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Favorite> findByPage(int uid, int start, int pageSize) throws InvocationTargetException, IllegalAccessException {
        String sql = "SELECT * FROM tab_route tr,tab_favorite tf WHERE tf.rid=tr.rid AND tf.uid = ? limit ? ,?;";
        List<Map<String, Object>> maps = template.queryForList(sql, uid, start, pageSize);
        ArrayList<Favorite> favorites = new ArrayList<>();
        for (Map<String, Object> mm : maps) {
            Route rr = new Route();
            BeanUtils.populate(rr,mm);
            Favorite ff = new Favorite();
            BeanUtils.populate(ff,mm);
            ff.setRoute(rr);
            favorites.add(ff);
        }
        return favorites;
    }
}
