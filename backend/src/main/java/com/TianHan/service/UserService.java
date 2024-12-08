package com.TianHan.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.TianHan.exception.CustomException;
import com.TianHan.mapper.UserMapper;
import com.TianHan.pojo.Account;
import com.TianHan.pojo.User;
import com.TianHan.utils.JwtHelper;
import com.TianHan.utils.MD5Util;
import com.TianHan.utils.Result;
import com.TianHan.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;

    public List<User> findList() {
        List<User> users = userMapper.queryAll();
        log.info("查询全部数据:{}", users);
        return users;
    }

    public List<User> getUserData() {
        List<User> users = userMapper.selectAll(null);
        log.info("查询全部数据:{}", users);
        return users;
    }

    public User findById(int uid) {
        User user = userMapper.selectById(uid);
        log.info("查询id为{}的数据:{}", uid, user);
        return user;
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll(user);
        log.info("分页查询数据:{}", users);
        return PageInfo.of(users);
    }

    public int addUser(User user) {
        String username = user.getUsername();
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser != null) {
            throw new CustomException("500", "用户已存在");
        }
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("123456");
        }
        if (StrUtil.isBlank(user.getUsername())) {
            user.setUsername("user");
        }
        if (StrUtil.isBlank(user.getNickname())) {
            user.setNickname("用户");
        }
        if (StrUtil.isBlank(user.getGender())) {
            user.setGender("男");
        }
        if (StrUtil.isBlank(user.getOccupation())) {
            user.setOccupation("用户");
        }
        int result = userMapper.insert(user);
        log.info("新增用户成功:{}", user);
        return result;
    }

    public int updateUser(User user) {
        int result = userMapper.update(user);
        log.info("更新用户成功:{}", user);
        return result;
    }

    public int deleteUser(Integer uid) {
        int result = userMapper.deleteById(uid);
        log.info("删除id为{}的用户成功", uid);
        return result;
    }

    public Result login(User user) {
        String username = user.getUsername();
        User dbUser = userMapper.selectByUsername(username);

        if (dbUser == null) {
            throw new CustomException("500", "用户不存在");
        }
        String password = user.getPassword();
//        if (!dbUser.getPassword().equals(password)) {
//            throw new CustomException("500", "用户名或密码错误");
//        }
        if (!dbUser.getPassword().equals(MD5Util.encrypt(password))) {
            throw new CustomException("500", "用户名或密码错误");
        }
        //账号密码正确
        //根据用户唯一标识生成token
        String token = TokenUtils.generateToken(String.valueOf(dbUser.getUid()), dbUser.getPassword());
        dbUser.setToken(token);

        return Result.success(dbUser);
    }


    public void signup(User user) {
        String username = user.getUsername();
        User dbUser = userMapper.selectByUsername(username);
        if (dbUser != null) {
            throw new CustomException("500", "用户已存在");
        }
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setNickname("用户" + RandomUtil.randomString(6));
        user.setGender("男");
        user.setOccupation("用户");
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        user.setDepartmentId(1);
        userMapper.insert(user);
    }

//    public Account updatePassword(Account account) {
//        Integer uid = account.getUid();
//        User dbUser = userMapper.selectById(uid);
//        if (dbUser == null) {
//            throw new CustomException("500", "用户不存在");
//        }
//        String password = MD5Util.encrypt(account.getPassword());
//        dbUser.setPassword(password);
//        userMapper.update(dbUser);
//        return dbUser;
//    }

    public Result updatePassword(User user) {
        Integer uid = user.getUid();
        User dbUser = userMapper.selectById(uid);
        if (dbUser == null) {
            throw new CustomException("500", "用户不存在");
        }
        String password = MD5Util.encrypt(user.getPassword());
        dbUser.setPassword(password);
        userMapper.update(dbUser);
        return Result.success(dbUser);
    }

    public boolean batchDeleteUser(List<Integer> uids) {
        for (int uid : uids) {
            int result = this.deleteUser(uid);
            if (result == 0) {
                log.error("删除id为{}的用户失败", uid);
                return false;
            }
        }
        log.info("批量删除用户成功:{}", uids);
        return true;
    }
}
