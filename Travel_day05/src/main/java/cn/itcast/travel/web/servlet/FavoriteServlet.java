package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author MXXIV
 *
 * 处理收藏的Servlet请求
 */
@WebServlet("/favorite/*")
public class FavoriteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    public void favoriteRank(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String maxmoney = request.getParameter("maxmoney");
        String minmoney = request.getParameter("minmoney");

        //当前页码，如果不传递，则默认为第一页
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0 && !"null".equals(currentPage)) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        //每页显示条数，如果不传递，默认每页显示12条记录
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0 && !"null".equals(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 8;
        }

        //3. 调用service查询PageBean对象
        PageBean<Favorite> pb = null;
        try {
            pb = favoriteService.pageQueryRank(currentPage, pageSize, rname, maxmoney, minmoney);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("######" + writeValueAsString(pb));
        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);
    }

    /**
     * 分页查询用户收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findByUid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String cidStr = request.getParameter("uid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        int uid = 0;
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            uid = Integer.parseInt(cidStr);
        }
        //当前页码，如果不传递，则默认为第一页
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        //每页显示条数，如果不传递，默认每页显示12条记录
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 12;
        }

        //3. 调用service查询PageBean对象
        PageBean<Favorite> pb = null;
        try {
            pb = favoriteService.pageQuery(uid, currentPage, pageSize);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(writeValueAsString(pb));
        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            uid = 0;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        // 用户没有登录 return 2001
        // 用户登录了  但是没有收藏  2002
        // 用户登录了 并且收藏了  2003

        //3. 调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4. 写回客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            return;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用service添加
        favoriteService.add(rid, uid);
    }

}
