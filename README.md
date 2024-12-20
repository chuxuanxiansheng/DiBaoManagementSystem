## 前提

本地安装`nodejs`

`application.yaml`配置文件：

```yaml
# server配置
server:
  port: 8080
  servlet:
    context-path: /
ip: localhost
spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql://${ip}:3306/TIANDI

      #      url: jdbc:mysql:///TIANDI
      username: root
      password: xxxxx
      driver-class-name: com.mysql.cj.jdbc.Driver

mybatis:
  type-aliases-package: com.TianHan.pojo # 配置别名
  mapper-locations: classpath:/mappers/*.xml # mapper xml位置
  configuration:  # setting配置
    auto-mapping-behavior: full
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl
```

注意：这里需要自定义部分内容

## 启动

`cd frontend`执行`npm install`然后执行`npm run dev`启动前端

打开`Main.java`启动后端


## SQL脚本

```sql
CREATE DATABASE tiandi;

USE tiandi;


CREATE TABLE `users`  (
  `uid` INT NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录名',
  `password` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录密码密文',
  `nickname` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `gender` VARCHAR(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户性别',
  `occupation` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户职业',
  `departmentId` INT NOT NULL COMMENT '部门ID',
  `avatar` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC; 

SELECT * FROM `users`;
 
INSERT INTO `users` VALUES(1, "baozi", "qweasd", "包子", "男", "用户", 1);
INSERT INTO `users` VALUES(2, "xiaolizi", "qweasd", "小李子", "男", "用户", 1);
 
 CREATE TABLE `admins`  (
  `uid` INT NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `username` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员登录名',
  `password` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员登录密码密文',
  `nickname` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员昵称',
  `gender` VARCHAR(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员性别',
  `occupation` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员职业',
  `avatar` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `adminname_unique`(`adminname`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC; 

SELECT * FROM admins;

INSERT INTO `admins` VALUES(1, "dabaozi", "qweasd", "包子", "男", "管理员");
INSERT INTO `admins` VALUES(2, "xiaolizi", "qweasd", "小李子", "男", "管理员");



CREATE TABLE articles(
id INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
title VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题', 
img VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '封面',
`description` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介', 
content LONGTEXT COLLATE utf8mb4_unicode_ci COMMENT '内容',
TIME VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布时间', 
PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章信息';

SELECT * FROM articles;


CREATE TABLE departments(
id INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名',
PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息';

SELECT * FROM departments;

INSERT INTO departments VALUES(1, "行政部");
INSERT INTO departments VALUES(2, "财务部");
INSERT INTO departments VALUES(3, "技术部");
INSERT INTO departments VALUES(4, "后勤部");
INSERT INTO departments VALUES(5, "法务部");
```

