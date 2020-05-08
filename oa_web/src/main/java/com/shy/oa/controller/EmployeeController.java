package com.shy.oa.controller;

import com.shy.oa.biz.DepartmentBiz;
import com.shy.oa.biz.EmployeeBiz;
import com.shy.oa.entity.Employee;
import com.shy.oa.global.Contant;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author zwm
 * @create 2020/5/8  17:44
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeBiz employeeBiz;

    @Autowired
    private DepartmentBiz departmentBiz;

    //列表
    @RequestMapping("list")
    public String list(Map<String,Object> map){
        map.put("list", employeeBiz.getAll());
        return "employee_list";
    }


    //跳转添加页
    @RequestMapping("/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("employee",new Employee());
        //需要传递部门
        map.put("dlist",departmentBiz.getAll());
        //传递所有职位
        map.put("plist", Contant.getPosts());
        return "employee_add";
    }

    //增加
    @RequestMapping("/add")
    public String add(Employee employee){
        employeeBiz.add(employee);
        return "redirect:list";
    }


    //删除
    @RequestMapping(value = "/remove",params = "sn")
    public String remove(String sn){
        employeeBiz.remove(sn);
        return "redirect:list";
    }



    //跳转修改页面
    @RequestMapping(value = "/to_update",params = "sn")
    public String toUpdate(String sn,Map<String,Object> map){
        map.put("employee",employeeBiz.get(sn));
        map.put("dlist",departmentBiz.getAll());
        //传递所有职位
        map.put("plist", Contant.getPosts());
        return "employee_update";
    }

    //修改
    @RequestMapping(value = "/update")
    public String update(Employee employee){
        employeeBiz.edit(employee);
        return "redirect:list";
    }




}
