package com.TianHan.utils;

import cn.hutool.core.util.StrUtil;
import com.TianHan.exception.CustomException;
import com.TianHan.mapper.UserMapper;
import com.TianHan.pojo.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
        //放行OPTIONS请求
//        String method = request.getMethod();
//        if ("OPTIONS".equals(method)) {
//            return true;
//        }

        String token = request.getHeader("token"); //从header中获取token
        //System.out.println("token: " + token);
        if(StrUtil.isBlank(token)){
            token = request.getParameter("token"); //从参数中获取token
        }
        //System.out.println("token2: " + token);
        //如果不是映射到方法直接通过
        if(handler instanceof HandlerMethod){
            AuthAccess authAccess = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if(authAccess != null) {
                return true;
            }
        }
        //System.out.println("handler: " + handler);
        if(StrUtil.isBlank(token)){
            throw new CustomException("401", "请登录");
        }
        //获取token中的用户信息，并设置到request里，后续根据用户信息进行相关操作
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            throw new CustomException("401", "token验证失败");
        }
        // 根据token中的用户id查询用户信息
        User user = userMapper.selectById(Integer.valueOf(userId));
        if(user == null){
            throw new CustomException("401", "用户不存在，请登录");
        }
        //通过用户密码加密之后生成一个验证器
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            verifier.verify(token);
        } catch (Exception e) {
            throw new CustomException("401", "token验证失败");
        }
        return true;
    }
}
