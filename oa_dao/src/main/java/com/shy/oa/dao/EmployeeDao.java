package com.shy.oa.dao;

import com.shy.oa.entity.Employee;
import org.apache.ibatis.annotations.Param;
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


    //这是做到提交报销单的时候才有的
    //查询部门和职位
    List<Employee> selectByDepartmentAndPost(@Param("dsn") String dsn,@Param("post") String post);


}
