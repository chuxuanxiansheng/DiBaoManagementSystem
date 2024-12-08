package com.TianHan.service;

import com.TianHan.mapper.GodMapper;
import com.TianHan.pojo.God;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GodService {
    @Autowired
    private GodMapper godMapper;

    public List<God> findList(){
        List<God> gods = godMapper.queryAll();
        log.info("查询全部数据:{}",gods);
        return gods;
    }

    public God findById(int id){
        God god = godMapper.selectById(id);
        log.info("查询id为{}的数据:{}",id,god);
        return god;
    }

    public PageInfo<God> selectPage(God god, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<God> gods = godMapper.selectAll(god);
        log.info("分页查询数据:{}",gods);
        return PageInfo.of(gods);
    }

    public int addGod(God god){
        int result = godMapper.insert(god);
        log.info("新增用户成功:{}",god);
        return result;
    }

    public int updateGod(God god){
        int result = godMapper.update(god);
        log.info("更新用户成功:{}",god);
        return result;
    }

    public int deleteGod(int id){
        int result = godMapper.deleteById(id);
        log.info("删除id为{}的用户成功",id);
        return result;
    }

    public boolean batchDeleteGod(List<Integer> ids) {
        for (int id : ids) {
            int result = this.deleteGod(id);
            if (result == 0) {
                log.error("删除id为{}的用户失败", id);
                return false;
            }
        }
        log.info("批量删除用户成功:{}", ids);
        return true;
    }
}
