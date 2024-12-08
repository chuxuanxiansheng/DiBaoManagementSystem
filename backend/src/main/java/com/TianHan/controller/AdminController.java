package com.TianHan.controller;

import com.TianHan.mapper.AdminMapper;
import com.TianHan.pojo.Admin;
import com.TianHan.service.AdminService;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @GetMapping("/list")
    @ResponseBody
    public Result getAdmin(){
        List<Admin> adminList = adminService.findList();
        log.info("查询的admin数据为:{}",adminList);
        return Result.success(adminList);
    }

    @GetMapping("/selectById/{uid}")
    @ResponseBody
    public Result getAdminById(@PathVariable("uid") Integer uid){
        Admin admin = adminService.findById(uid);
        log.info("查询的admin数据为:{}",admin);
        if(admin == null){
            return Result.error("404","用户不存在");
        }
        return Result.success(admin);
    }
    @PostMapping("/insert")
    @ResponseBody
    public Result insertAdmin(@RequestBody Admin admin){
        int result = adminService.addAdmin(admin);
        if(result == 1){
            log.info("插入成功");
            return Result.success();
        }else{
            log.info("插入失败");
            return Result.error();
        }
    }
    @DeleteMapping("/delete/{uid}")
    @ResponseBody
    public Result deleteAdmin(@PathVariable("uid") int uid){
        int result = adminService.deleteAdmin(uid);
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
    public Result updateAdmin(@RequestBody Admin admin){
        int result = adminService.updateAdmin(admin);
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
    public Result selectPage( Admin admin, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Admin> adminPageInfo = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(adminPageInfo);
    }

    @DeleteMapping("/batchDelete")
    public Result batchDeleteAdmin(@RequestBody List<Integer> ids){
        boolean result = adminService.batchDeleteAdmin(ids);
        if(result){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }

}
