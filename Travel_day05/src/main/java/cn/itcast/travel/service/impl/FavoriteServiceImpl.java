package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.service.FavoriteService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();
    /**
     * 判断用户是否收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
        //如果对象有值，则为true，反之，则为false
        return favorite != null;
    }

    @Override
    public PageBean<Favorite> pageQueryRank(int currentPage, int pageSize, String rname, String maxmoney, String minmoney) throws InvocationTargetException, IllegalAccessException {
        //封装PageBean
        PageBean<Favorite> pb = new PageBean<Favorite>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = favoriteDao.findTotalCountRank(rname,maxmoney,minmoney);
        pb.setTotalCount(totalCount);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        pb.setTotalPage(totalPage);

        //设置当前页显示的数据集合
        //开始的记录数
        int start = (currentPage - 1) * pageSize;
        List<Favorite> list = favoriteDao.findByPageRank(start,pageSize,rname,maxmoney,minmoney);
        pb.setList(list);
        return pb;
    }

    /**
     * 用户添加收藏
     * @param rid
     * @param uid
     */
    @Override
    public void add(String rid, int uid) {
        // 开启事务控制
        // 像收藏表中插入一条数据
        favoriteDao.add(Integer.parseInt(rid), uid);
        // 根据rid修改对应route表中的count字段
        // 根据rid查询route route.setCount(route.getCount()+1); route
        // favoriteDao.update(Integer.parseInt(rid));
        // 直接根据rid去修改count
        // 提交事务
    }

    @Override
    public PageBean<Favorite> pageQuery(int uid, int currentPage, int pageSize) throws InvocationTargetException, IllegalAccessException {
        //封装PageBean
        PageBean<Favorite> pb = new PageBean<Favorite>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = favoriteDao.findTotalCount(uid);
        pb.setTotalCount(totalCount);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;

        pb.setTotalPage(totalPage);

        //设置当前页显示的数据集合
        //开始的记录数
        int start = (currentPage - 1) * pageSize;
        List<Favorite> list = favoriteDao.findByPage(uid,start,pageSize);
        pb.setList(list);
        return pb;
    }
}
