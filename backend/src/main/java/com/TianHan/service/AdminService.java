package com.TianHan.service;

import cn.hutool.core.util.StrUtil;
import com.TianHan.exception.CustomException;
import com.TianHan.mapper.AdminMapper;
import com.TianHan.pojo.Account;
import com.TianHan.pojo.Admin;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    public List<Admin> findList(){
        List<Admin> admin = adminMapper.queryAll();
        log.info("查询全部数据:{}",admin);
        return admin;
    }

    public Admin findById(int id){
        Admin admin = adminMapper.selectById(id);
        log.info("查询id为{}的数据:{}",id,admin);
        return admin;
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> admins = adminMapper.selectAll(admin);
        log.info("分页查询数据:{}",admins);
        return PageInfo.of(admins);
    }

    public int addAdmin(Admin admin){
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) {
            throw new CustomException("500", "用户已存在");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("admin");
        }
        if(StrUtil.isBlank(admin.getUsername())){
            admin.setUsername("admin");
        }
        if(StrUtil.isBlank(admin.getNickname())){
            admin.setNickname("管理员");
        }
        if(StrUtil.isBlank(admin.getGender())){
            admin.setGender("男");
        }
        if(StrUtil.isBlank(admin.getOccupation())){
            admin.setOccupation("管理员");
        }
        int result = adminMapper.insert(admin);
        log.info("新增用户成功:{}",admin);
        return result;
    }

    public int updateAdmin(Admin admin){
        int result = adminMapper.update(admin);
        log.info("更新用户成功:{}",admin);
        return result;
    }

    public int deleteAdmin(int id){
        int result = adminMapper.deleteById(id);
        log.info("删除id为{}的用户成功",id);
        return result;
    }

    public Result login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {
            throw new CustomException("500", "用户不存在");
        }
        String password = account.getPassword();
        if (!dbAdmin.getPassword().equals(password)) {
            throw new CustomException("500", "用户名或密码错误");
        }
        return Result.success(dbAdmin);
    }

    public void signup(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) {
            throw new CustomException("500", "用户已存在");
        }
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword("admin");
        }
        if(StrUtil.isBlank(admin.getNickname())){
            admin.setNickname("管理员");
        }
        if(StrUtil.isBlank(admin.getGender())){
            admin.setGender("男");
        }
        if(StrUtil.isBlank(admin.getOccupation())){
            admin.setOccupation("管理员");
        }
        adminMapper.insert(admin);
    }

    public Account updatePassword(Account account) {
        Integer uid = account.getUid();
        Admin dbUser = adminMapper.selectById(uid);
        if (dbUser == null) {
            throw new CustomException("500", "用户不存在");
        }

        String password = account.getPassword();
        dbUser.setPassword(password);
        adminMapper.update(dbUser);
        return dbUser;
    }

    public boolean batchDeleteAdmin(List<Integer> ids) {
        for (int id : ids) {
            int result = this.deleteAdmin(id);
            if (result == 0) {
                log.error("删除id为{}的用户失败", id);
                return false;
            }
        }
        log.info("批量删除用户成功:{}", ids);
        return true;
    }
}
