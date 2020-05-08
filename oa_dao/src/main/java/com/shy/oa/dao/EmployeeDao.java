package com.shy.oa.dao;

import com.shy.oa.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zwm
 * @create 2020/5/8  17:46
 */
@Repository
public interface EmployeeDao {


    void insert(Employee employee);

    void update(Employee employee);

    void delete(String sn);

    Employee select(String sn);

    List<Employee> selectAll();



}
