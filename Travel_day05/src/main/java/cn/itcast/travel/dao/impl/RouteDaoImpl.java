package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Route> findByPage2(int start, int pageSize) {
        String sql = "select * from tab_route where isThemeTour = ? order by rdate limit ? , ?";
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),1,start,pageSize);
    }

    @Override
    public List<Route> findByPage(int start, int pageSize) {
        String sql = "select * from tab_route order by rdate limit ? , ?";
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),start,pageSize);
    }

    @Override
    public int findTotalCount(int cid,String rname) {
        //String sql = "select count(*) from tab_route where cid = ? ";
        //1.定义sql模板
        /*
            select count(*) from tab_route  where cid = ? where rname like ?
            技巧
        */
        String sql = "select count(*) from tab_route WHERE  1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");
            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }

        sql = sb.toString();
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        //String sql = "select * from tab_route where cid = ? and rname like ?  limit ? , ?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");
            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        /*String sql = "SELECT * FROM tab_route tr,tab_seller ts,tab_route_img ti WHERE tr.sid=ts.sid AND tr.rid=ti.rid AND tr.rid = ?";
        List<Map<String, Object>> maps = template.queryForList(sql, rid);
        Route route = new Route();
        Seller seller = new Seller();
        List<RouteImg> routeImgList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            try {
                // BeanUtils内部使用的是内省  内省是基于反射的
                BeanUtils.populate(route,map);// 20
                BeanUtils.populate(seller,map);// 20
                RouteImg routeImg = new RouteImg();
                BeanUtils.populate(routeImg,map);
                routeImgList.add(routeImg);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        route.setSeller(seller);
        route.setRouteImgList(routeImgList);
        return route;*/
       return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}
