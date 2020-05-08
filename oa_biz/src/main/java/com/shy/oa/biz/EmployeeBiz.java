package com.shy.oa.biz;



import com.shy.oa.entity.Employee;

import java.util.List;

/**
 * @author zwm
 * @create 2020/5/8  16:53
 */
public interface EmployeeBiz {

    void add(Employee employee);

    void edit(Employee employee);

    void remove(String sn);

    Employee get(String sn);

    List<Employee> getAll();




}
