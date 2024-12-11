/*
 Navicat Premium Data Transfer

 Source Server         : 3
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : tiandi

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 11/12/2024 15:07:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员登录名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员登录密码密文',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员昵称',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员性别',
  `occupation` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员职业',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (1, 'dabaozi', '8025d0683f2f8d4a387b7f49b3af5c37', '包子', '男', '管理员', NULL);
INSERT INTO `admins` VALUES (2, 'xiaolizi', '36f17c3939ac3e7b2fc9396fa8e953ea', '小李子', '男', '管理员', NULL);

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `category_id` int NULL DEFAULT NULL COMMENT '分类ID',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览量',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  `author_id` int NOT NULL COMMENT '作者ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_view_count`(`view_count` ASC) USING BTREE,
  INDEX `idx_author_id`(`author_id` ASC) USING BTREE,
  CONSTRAINT `fk_article_author` FOREIGN KEY (`author_id`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of articles
-- ----------------------------
INSERT INTO `articles` VALUES (1, '社会治理数字化转型', '3.png', '智慧社区建设成效显著', '探讨社会治理新模式...', 7, 444, 4, '2024-03-27 21:00:00', 1);
INSERT INTO `articles` VALUES (2, '军事装备现代化升级', '3.png', '新型装备提升作战能力', '报道军事科技发展...', 3, 794, 2, '2024-03-27 20:00:00', 2);
INSERT INTO `articles` VALUES (3, '金融科技改革支付方式', '3.png', '数字货币试点范围扩大', '分析金融科技发展趋势...', 8, 567, 1, '2024-03-27 19:00:00', 1);
INSERT INTO `articles` VALUES (4, '教育科技创新教学模式', '4.png', 'AI助手提升学习效率', '介绍最新教育科技应用案例...', 1, 345, 1, '2024-03-27 15:00:00', 1);
INSERT INTO `articles` VALUES (5, '远程医疗服务普及化', '4.png', '互联网医疗平台用户突破1亿', '分析远程医疗发展现状...', 2, 423, 1, '2024-03-27 16:00:00', 2);
INSERT INTO `articles` VALUES (6, '量子计算研究新进展', '4.png', '量子计算机运算能力再创新高', '详解量子计算技术突破...', 7, 678, 1, '2024-03-27 17:00:00', 1);
INSERT INTO `articles` VALUES (7, '体育科技助力奥运备战', '4.png', '科技设备助力运动员训练', '介绍体育科技最新应用...', 5, 234, 1, '2024-03-27 18:00:00', 2);
INSERT INTO `articles` VALUES (8, '金融科技改革支付方式', '3.png', '数字货币试点范围扩大', '分析金融科技发展趋势...', 8, 567, 1, '2024-03-27 19:00:00', 1);
INSERT INTO `articles` VALUES (9, '军事装备现代化升级', '3.png', '新型装备提升作战能力', '报道军事科技发展...', 3, 789, 1, '2024-03-27 20:00:00', 2);
INSERT INTO `articles` VALUES (10, '社会治理数字化转型', '3.png', '智慧社区建设成效显著', '探讨社会治理新模式...', 4, 432, 1, '2024-03-27 21:00:00', 1);
INSERT INTO `articles` VALUES (12, '111', NULL, '2222', '<p>323233</p>', 5, 4, 1, '2024-12-11 12:27:16', 1);

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, '教育', 1);
INSERT INTO `categories` VALUES (2, '健康', 2);
INSERT INTO `categories` VALUES (3, '军事', 3);
INSERT INTO `categories` VALUES (4, '社会', 4);
INSERT INTO `categories` VALUES (5, '体育', 5);
INSERT INTO `categories` VALUES (6, '娱乐', 6);
INSERT INTO `categories` VALUES (7, '科技', 7);
INSERT INTO `categories` VALUES (8, '财经', 8);

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `article_id` int NOT NULL COMMENT '文章ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_article_id`(`article_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1, 1, 1, '这是一条评论内容', '2024-03-27 10:00:00');
INSERT INTO `comments` VALUES (2, 1, 2, '这是另一条评论', '2024-03-27 11:00:00');
INSERT INTO `comments` VALUES (3, 3, 1, '评论内容1', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (4, 4, 2, '评论内容2', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (5, 5, 1, '评论内容3', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (6, 6, 2, '评论内容4', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (7, 7, 1, '评论内容5', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (8, 8, 2, '评论内容6', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (9, 9, 1, '评论内容7', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (10, 10, 2, '评论内容8', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (11, 2, 1, '评论内容9', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (12, 1, 2, '评论内容10', '2024-12-11 10:37:39');
INSERT INTO `comments` VALUES (13, 2, 1, '评论内容11', '2024-12-11 10:43:46');
INSERT INTO `comments` VALUES (14, 12, 1, '1232', '2024-12-11 12:30:43');
INSERT INTO `comments` VALUES (15, 1, 1, '2', '2024-12-11 12:49:47');

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '行政部');
INSERT INTO `departments` VALUES (2, '财务部');
INSERT INTO `departments` VALUES (3, '技术部');
INSERT INTO `departments` VALUES (4, '后勤部');
INSERT INTO `departments` VALUES (5, '法务部');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户登录密码密文',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户性别',
  `occupation` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户职业',
  `departmentId` int NOT NULL COMMENT '部门ID',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'baozi', '36f17c3939ac3e7b2fc9396fa8e953ea', '包子', '男', '用户', 1, 'touxiang.png');
INSERT INTO `users` VALUES (2, 'xiaolizi', '36f17c3939ac3e7b2fc9396fa8e953ea', '小李子', '男', '用户', 1, 'touxiang2.png');

-- ----------------------------
-- Triggers structure for table comments
-- ----------------------------
DROP TRIGGER IF EXISTS `update_comment_count_after_insert`;
delimiter ;;
CREATE TRIGGER `update_comment_count_after_insert` AFTER INSERT ON `comments` FOR EACH ROW BEGIN
    UPDATE `articles`
    SET `comment_count` = `comment_count` + 1
    WHERE `id` = NEW.`article_id`;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
