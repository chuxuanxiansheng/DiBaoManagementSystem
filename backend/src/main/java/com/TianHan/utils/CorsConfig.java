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
