package com.TianHan.mapper;

import com.TianHan.pojo.User;

import java.util.List;

public interface UserMapper {

    List<User> queryAll();

    int insert(User user);
    int update(User user);
    int deleteById(Integer uid);
    User selectById(Integer uid);

    List<User> selectAll();

    List<User> selectAll(User user);

    User selectByUsername(String username);
}
