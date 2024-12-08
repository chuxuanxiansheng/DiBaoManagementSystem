package com.TianHan.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.TianHan.mapper.UserMapper;
import com.TianHan.pojo.User;
import com.TianHan.service.UserService;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    @ResponseBody
    public Result getUser() {
        List<User> userList = userService.findList();
        log.info("查询的user数据为:{}", userList);
        return Result.success(userList);
    }

    @GetMapping("/selectById/{uid}")
    @ResponseBody
    public Result getUserById(@PathVariable("uid") Integer uid) {
        User user = userService.findById(uid);
        log.info("查询的user数据为:{}", user);
        if (user == null) {
            return Result.error("404", "用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping("/insert")
    @ResponseBody
    public Result insertUser(@RequestBody User user) {
        int result = userService.addUser(user);
        if (result == 1) {
            log.info("插入成功");
            return Result.success();
        } else {
            log.info("插入失败");
            return Result.error();
        }
    }

    @DeleteMapping("/delete/{uid}")
    @ResponseBody
    public Result deleteUser(@PathVariable("uid") int uid) {
        log.info("删除id为{}的用户", uid);
        int result = userService.deleteUser(uid);
        if (result == 1) {
            log.info("删除成功");
            return Result.success();
        } else {
            log.info("删除失败");
            return Result.error();
        }
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        if (result == 1) {
            log.info("更新成功");
            return Result.success();
        } else {
            log.info("更新失败");
            return Result.error();
        }
    }

    @GetMapping("/selectPage")
    @ResponseBody
    public Result selectPage(User user, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<User> userPageInfo = userService.selectPage(user, pageNum, pageSize);
        return Result.success(userPageInfo);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<User> userList = userService.findList();

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("gender", "性别");
        writer.addHeaderAlias("occupation", "职业");
        writer.addHeaderAlias("departmentName", "部门名称");
        //默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(userList, true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset = UTF-8");
        //设置文件名
        String fileName = URLEncoder.encode("用户信息", StandardCharsets.UTF_8) ;  //中文文件名需要进行URL编码
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        out.close();
    }
    @PostMapping("/batchInsert")
    public Result batchInsert(MultipartFile file) throws Exception {
        //拿到输入流构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        //读取数据
        reader.addHeaderAlias("用户名", "username");
        reader.addHeaderAlias("性别", "gender");
        reader.addHeaderAlias("职业", "occupation");
        List<User> userList = reader.readAll(User.class);


        //批量插入
        for (User user : userList) {
            userService.addUser(user);
        }
        return Result.success();
    }

    @DeleteMapping("/batchDelete")
    public Result batchDeleteUser(@RequestBody List<Integer> ids){
        boolean result = userService.batchDeleteUser(ids);
        if(result){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }
}
