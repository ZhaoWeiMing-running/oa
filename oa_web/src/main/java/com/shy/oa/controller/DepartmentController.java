package com.shy.oa.controller;

import com.shy.oa.biz.DepartmentBiz;
import com.shy.oa.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author zwm
 * @create 2020/5/8  16:58
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {


    @Autowired
    private DepartmentBiz departmentBiz;

    //列表
    @RequestMapping("list")
    public String list(Map<String,Object> map){
        map.put("list", departmentBiz.getAll());
        return "department_list";
    }


    //跳转添加页
    @RequestMapping("/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("department",new Department());
        return "department_add";
    }

    //增加
    @RequestMapping("/add")
    public String add(Department department){
        departmentBiz.add(department);
        return "redirect:list";
    }


    //删除
    @RequestMapping(value = "/remove",params = "sn")
    public String remove(String sn){
        departmentBiz.remove(sn);
        return "redirect:list";
    }



    //跳转修改页面
    @RequestMapping(value = "/to_update",params = "sn")
    public String toUpdate(String sn,Map<String,Object> map){
        map.put("department",departmentBiz.get(sn));
        return "department_update";
    }

    //修改
    @RequestMapping(value = "/update")
    public String update(Department department){
        departmentBiz.edit(department);
        return "redirect:list";
    }






}
