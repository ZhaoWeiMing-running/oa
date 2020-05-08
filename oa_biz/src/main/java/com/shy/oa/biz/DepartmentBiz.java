package com.shy.oa.biz;

import com.shy.oa.entity.Department;

import java.util.List;

/**
 * @author zwm
 * @create 2020/5/8  16:53
 */
public interface DepartmentBiz {

    void add(Department department);

    void edit(Department department);

    void remove(String sn);

    Department get(String sn);

    List<Department> getAll();




}
