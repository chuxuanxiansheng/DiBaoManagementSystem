package com.TianHan.utils;

import cn.hutool.core.date.DateUtil;
import com.TianHan.mapper.UserMapper;
import com.TianHan.pojo.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {
    private static UserMapper staticUserMapper;

    @Resource
    UserMapper userMapper;

    @PostConstruct
    public void setUserService() {
        staticUserMapper = userMapper;
    }

    //生成token
    public static String generateToken(String userId, String sign){
        return JWT.create().withAudience(userId).withExpiresAt(DateUtil.offsetHour(new Date(), 2)).sign(Algorithm.HMAC256(sign));//使用password作为密钥
    }

    //获取当前登录用户的信息
    public static User getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StringUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserMapper.selectById(Integer.parseInt(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
