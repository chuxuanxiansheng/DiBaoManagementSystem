package com.TianHan.controller;

import com.TianHan.mapper.DepartmentMapper;
import com.TianHan.pojo.Department;
import com.TianHan.service.DepartmentService;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @GetMapping("/list")
    @ResponseBody
    public Result getDepartment(){
        List<Department> departmentList = departmentService.findList();
        log.info("查询的department数据为:{}",departmentList);
        return Result.success(departmentList);
    }

    @GetMapping("/selectById/{id}")
    @ResponseBody
    public Result getDepartmentById(@PathVariable("id") Integer id){
        Department department = departmentService.findById(id);
        log.info("查询的department数据为:{}",department);
        if(department == null){
            return Result.error("404","用户不存在");
        }
        return Result.success(department);
    }
    @PostMapping("/insert")
    @ResponseBody
    public Result insertDepartment(@RequestBody Department department){
        int result = departmentService.addDepartment(department);
        if(result == 1){
            log.info("插入成功");
            return Result.success();
        }else{
            log.info("插入失败");
            return Result.error();
        }
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Result deleteDepartment(@PathVariable("id") int id){
        int result = departmentService.deleteDepartment(id);
        if(result == 1){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }
    @PutMapping("/update")
    @ResponseBody
    public Result updateDepartment(@RequestBody Department department){
        int result = departmentService.updateDepartment(department);
        if(result == 1){
            log.info("更新成功");
            return Result.success();
        }else{
            log.info("更新失败");
            return Result.error();
        }
    }

    @GetMapping("/selectPage")
    @ResponseBody
    public Result selectPage( Department department, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Department> departmentPageInfo = departmentService.selectPage(department, pageNum, pageSize);
        return Result.success(departmentPageInfo);
    }
}
