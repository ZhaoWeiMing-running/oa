package com.shy.oa.global;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //如果是访问登陆界面 放行
        String url = request.getRequestURI();
        if (url.toLowerCase().indexOf("login")>=0){
            return true;
        }

        //通过session获取当前对象，如果当前有登陆用户，放行
        HttpSession session = request.getSession();
        if (session.getAttribute("employee")!=null){
            return true;
        }
        //跳转到登录页
        response.sendRedirect("/to_login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
