package com.TianHan.mapper;

import com.TianHan.pojo.Department;

import java.util.List;

public interface DepartmentMapper {

    List<Department> queryAll();

    int insert(Department department);
    int update(Department department);
    int deleteById(Integer id);
    Department selectById(Integer id);
    Department selectByName(String name);

    List<Department> selectAll(Department department);
}
