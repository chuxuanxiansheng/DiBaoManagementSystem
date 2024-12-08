package com.TianHan.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  //当前跨域请求最大有效时长，单位为秒，默认为一天
  private static final long MAX_AGE = 86400L;

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.addAllowedOrigin("*");
    corsConfiguration.setMaxAge(MAX_AGE);
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(source);
  }
}

//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**").allowedOrigins("*") // 允许所有域名访问
//      .allowedMethods("*") // 允许所有请求方法
//      .allowedHeaders("*") // 允许所有请求头
//      .exposedHeaders("*"); //暴露所有响应头
//  }
//
//}
/**
 * 全局跨域解决
 */
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**").
//            allowedOrigins("*"). //允许跨域的域名，可以用*表示允许任何域名使用
//            allowedMethods("*"). //允许任何方法（post、get等）
//            allowedHeaders("*"). //允许任何请求头
//            allowCredentials(true). //带上cookie信息
//            exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
//  }
//}
