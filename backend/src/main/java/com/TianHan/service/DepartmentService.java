package com.TianHan.service;

import com.TianHan.mapper.DepartmentMapper;
import com.TianHan.pojo.Department;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    public List<Department> findList(){
        List<Department> departments = departmentMapper.queryAll();
        log.info("查询全部数据:{}",departments);
        return departments;
    }

    public Department findById(int id){
        Department department = departmentMapper.selectById(id);
        log.info("查询id为{}的数据:{}",id,department);
        return department;
    }

    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Department> departments = departmentMapper.selectAll(department);
        log.info("分页查询数据:{}",departments);
        return PageInfo.of(departments);
    }

    public int addDepartment(Department department){
        int result = departmentMapper.insert(department);
        log.info("新增用户成功:{}",department);
        return result;
    }

    public int updateDepartment(Department department){
        int result = departmentMapper.update(department);
        log.info("更新用户成功:{}",department);
        return result;
    }

    public int deleteDepartment(int id){
        int result = departmentMapper.deleteById(id);
        log.info("删除id为{}的用户成功",id);
        return result;
    }
}
