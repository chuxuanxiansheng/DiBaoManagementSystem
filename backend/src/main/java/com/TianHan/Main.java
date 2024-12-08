package com.TianHan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.TianHan.mapper")
@SpringBootApplication()
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}