package com.TianHan.mapper;

import com.TianHan.pojo.God;

import java.util.List;

public interface GodMapper {
    List<God> queryAll();

    int insert(God god);
    int update(God god);
    int deleteById(Integer id);
    God selectById(Integer id);

    List<God> selectAll(God god);
}
