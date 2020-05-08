package com.shy.oa.dao;

import com.shy.oa.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zwm
 * @create 2020/5/8  16:42
 */
@Repository
public interface DepartmentDao {


    void insert(Department department);

    void update(Department department);

    void delete(String sn);

    Department select(String sn);

    List<Department> selectAll();



}
