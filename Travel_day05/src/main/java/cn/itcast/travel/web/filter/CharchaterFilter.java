package cn.itcast.travel.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 解决全站乱码问题，处理所有的请求
 */
//@WebFilter("/*")
public class CharchaterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {
        //将父接口转为子接口
        final HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
        HttpServletRequest rr = null;
        //获取请求方式  get/post
        String method = request.getMethod();
        //解决post请求中文数据乱码问题
        if(method.equalsIgnoreCase("post")){
            request.setCharacterEncoding("utf-8");
        }else if (method.equalsIgnoreCase("get")){
            rr = (HttpServletRequest) Proxy.newProxyInstance(request.getClass().getClassLoader(),
                    request.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if ("getParameter".equalsIgnoreCase(method.getName())){
                                // 这个是乱码的
                                String vv = (String) method.invoke(request,args);
                                // 处理乱码
                                vv = new String(vv.getBytes("iso-8859-1"),"utf-8");
                                return vv;
                            }
                            return method.invoke(request);
                        }
                    });
        }
        //处理响应乱码
        response.setContentType("text/html;charset=utf-8");
        filterChain.doFilter(rr,response);
    }

    @Override
    public void destroy() {

    }
}
