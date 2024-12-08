package com.TianHan.controller;


import com.TianHan.mapper.GodMapper;
import com.TianHan.pojo.God;
import com.TianHan.service.GodService;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/god")
public class GodController {
    @Autowired
    private GodService godService;

    @Autowired
    private GodMapper godMapper;

    @GetMapping("/list")
    @ResponseBody
    public Result getGod(){
        List<God> godList = godService.findList();
        log.info("查询的god数据为:{}",godList);
        return Result.success(godList);
    }

    @GetMapping("/find/{id}")
    @ResponseBody
    public Result getGodById(@PathVariable("id") int id){
        God god = godService.findById(id);
        log.info("查询的god数据为:{}",god);
        if(god == null){
            return Result.error("404","用户不存在");
        }
        return Result.success(god);
    }
    @PostMapping("/insert")
    @ResponseBody
    public Result insertGod(@RequestBody God god){
        int result = godService.addGod(god);
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
    public Result deleteGod(@PathVariable("id") int id){
        int result = godService.deleteGod(id);
        if(result == 1){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }

    @DeleteMapping("/batchDelete")
    public Result batchDeleteGod(@RequestBody List<Integer> ids){
        boolean result = godService.batchDeleteGod(ids);
        if(result){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateGod(@RequestBody God god){
        int result = godService.updateGod(god);
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
    public Result selectPage( God god, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<God> godPageInfo = godService.selectPage(god, pageNum, pageSize);
        return Result.success(godPageInfo);
    }

}
