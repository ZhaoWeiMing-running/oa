package com.shy.oa.biz;

import com.shy.oa.entity.Employee;

public interface GlobalBiz {

    //登录
    Employee login(String sn,String password);


    //修改密码
    void changePassword(Employee employee);

}
