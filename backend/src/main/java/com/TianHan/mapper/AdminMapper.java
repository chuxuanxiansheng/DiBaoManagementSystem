package com.TianHan.mapper;

import com.TianHan.pojo.Admin;

import java.util.List;

public interface AdminMapper {

    List<Admin> queryAll();

    int insert(Admin admin);
    int update(Admin admin);
    int deleteById(Integer uid);
    Admin selectById(Integer uid);

    List<Admin> selectAll(Admin admin);

    Admin selectByUsername(String username);
}
