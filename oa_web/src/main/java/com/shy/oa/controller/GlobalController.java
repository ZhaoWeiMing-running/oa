package com.shy.oa.controller;

import com.shy.oa.biz.GlobalBiz;
import com.shy.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class GlobalController {

    @Autowired
    private GlobalBiz globalBiz;

    //跳转登录页面
    @RequestMapping("to_login")
    public String toLogin(){
        return "login";
    }

    //登录页
    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam("sn") String sn, @RequestParam("password") String password){
        Employee employee = globalBiz.login(sn, password);
        //未登录
        if (employee==null){
            return "redirect:to_login";
        }
        //登陆成功,跳转到个人中心页面
        session.setAttribute("employee",employee);
        return "redirect:self";
    }

    //个人中心页面
    @RequestMapping("/self")
    public String self(){
        return "self";
    }


    //登出
    @RequestMapping("/quit")
    public String quit(HttpSession session){
        session.setAttribute("employee",null);
        //跳转到登录页
        return "redirect:to_login";
    }

    //跳转 修改密码页
    @RequestMapping("/to_change_password")
    public String changePassword(){
        return "change_password";
    }


    //修改密码
    @RequestMapping("/change_password")
    public String changePassword(HttpSession session,@RequestParam("old") String old,@RequestParam("new1") String new1,@RequestParam("new2") String new2){
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee.getPassword().equals(old)){
            if (new1.equals(new2)){
                employee.setPassword(new1);
                globalBiz.changePassword(employee);
                return "redirect:self";
            }
        }
        return "redirect:to_change_password";
    }



}
