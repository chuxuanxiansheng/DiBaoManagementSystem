package com.TianHan.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.TianHan.pojo.Article;
import com.TianHan.pojo.User;
import com.TianHan.service.ArticleService;
import com.TianHan.service.UserService;
import com.TianHan.utils.AuthAccess;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
public class WebController {

    @Resource
    private UserService userService;

    @Resource
    private ArticleService articleService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("500", "用户名或密码不能为空");
        }
        return userService.login(user);

    }

    @AuthAccess
    @PostMapping("/signup")
    public Result signup(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("500", "用户名或密码不能为空");
        }
        userService.signup(user);
        return Result.success();
    }

    @AuthAccess
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getEmail())) {
            return Result.error("500", "用户名或邮箱不能为空");
        }
        userService.resetPassword(user);
        return Result.success();
    }

    @AuthAccess
    @GetMapping("/show")
    @ResponseBody
    public Result getAllData(Article article, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize) {
        PageInfo<Article> articlePageInfo = articleService.findList(article, pageNum, pageSize);
        return Result.success(articlePageInfo);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user) {
        return userService.updatePassword(user);
    }

    @GetMapping("/getBarData")
    public Result getBarData() {
        Map<String, Object> map = new HashMap<>();
        List<User> userList = userService.getUserData();
        Set<String> departmentNameSet = userList.stream().map(User::getDepartmentName).collect(Collectors.toSet());
        map.put("department", departmentNameSet);
        List<Long> countList = new ArrayList<>();
        for (String departmentName : departmentNameSet) {
            long count = userList.stream().filter(user -> user.getDepartmentName().equals(departmentName)).count();
            countList.add(count);
        }
        map.put("count", countList);
        return Result.success(map);
    }

    @GetMapping("/getLineData")
    public Result getLineData() {

        Map<String, Object> map = new HashMap<>();

        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -7);
        List<DateTime> dateTimeList = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        //把DateTimeList转化为List<String>
        List<String> dateStrList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日")).sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        map.put("date", dateStrList);
        //获取每天的文章数量
        List<Integer> countList = new ArrayList<>();
        for (DateTime dateStr : dateTimeList) {
            String dateStrFormat = DateUtil.formatDate(dateStr); //格式化日期2021-08-01
            Integer count = articleService.getArticleCountByDate(dateStrFormat);
            countList.add(count);
        }
        map.put("count", countList);
        return Result.success(map);
    }

    @GetMapping("/getPieData")
    public Result getPieData() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<User> userList = userService.getUserData();
        Set<String> departmentNameSet = userList.stream().map(User::getDepartmentName).collect(Collectors.toSet());

        for (String departmentName : departmentNameSet) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", departmentName);
            long count = userList.stream().filter(user -> user.getDepartmentName().equals(departmentName)).count();
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }
}
