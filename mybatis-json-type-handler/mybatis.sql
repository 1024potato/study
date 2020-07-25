/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : mybatis

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 25/07/2020 11:57:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cate
-- ----------------------------
DROP TABLE IF EXISTS `cate`;
CREATE TABLE `cate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类图片url',
  `pid` int(11) NOT NULL DEFAULT 0 COMMENT '上级分类id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类路径',
  `sort` smallint(6) NOT NULL DEFAULT 0 COMMENT '排序 高 -> 低',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cate
-- ----------------------------
INSERT INTO `cate` VALUES (1, 'stri222ng', 'http://c.jpg', 0, '', 0, '2019-05-22 19:02:44', '2020-06-30 18:24:34');
INSERT INTO `cate` VALUES (2, '顶级分类1', 'http://b.jpg', 0, '', 10, '2019-05-22 19:05:50', '2019-05-22 19:05:50');
INSERT INTO `cate` VALUES (3, '二级分类2', 'http://b.jpg', 1, '1,', 0, '2019-05-22 20:32:10', '2019-05-23 19:11:12');
INSERT INTO `cate` VALUES (4, '三级分类3', '', 3, '1,3,', 0, '2019-05-22 20:52:58', '2019-05-22 20:52:58');
INSERT INTO `cate` VALUES (5, '四级分类4', '', 4, '1,3,4,', 0, '2019-05-23 18:38:32', '2019-05-23 18:38:32');
INSERT INTO `cate` VALUES (6, '二级分类25', '', 1, '1,', 0, '2019-05-23 19:10:11', '2019-05-23 19:10:11');
INSERT INTO `cate` VALUES (7, '三级分类26', '', 6, '1,6,', 0, '2019-05-23 19:10:35', '2019-05-23 19:10:35');
INSERT INTO `cate` VALUES (8, '顶级分类%', 'http://b.jpg', 0, '', 10, '2019-05-29 22:20:46', '2019-05-29 22:20:46');
INSERT INTO `cate` VALUES (9, '二级分类8', 'http://b.jpg', 8, '8,', 10, '2019-07-15 16:28:33', '2019-07-15 16:28:33');
INSERT INTO `cate` VALUES (10, '三级分类9', 'http://b.jpg', 9, '8,9,', 10, '2019-07-15 16:29:10', '2019-07-15 16:29:10');
INSERT INTO `cate` VALUES (216, '顶级分\'类1', 'http://b.jpg', 0, '', 10, '2020-07-20 16:12:48', '2020-07-20 16:12:48');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账号',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `mobile` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '头像',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `cate` json NULL,
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户状态 1 正常 0 禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 70 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '222222222222', 'string1', '15549459959', 'http://a.jpg', 'string', NULL, 1, '2019-12-02 00:00:00', '2019-08-08 20:05:48');
INSERT INTO `user` VALUES (2, 'string', 'string', '15549459959', 'http://a.jpg', 'string', NULL, 1, '2019-05-21 22:23:57', '2019-05-21 22:23:57');
INSERT INTO `user` VALUES (3, 'string', 'string', '15549459959', 'http://a.jpg', 'string', NULL, 1, '2019-05-21 22:23:57', '2019-05-21 22:23:57');
INSERT INTO `user` VALUES (10, 'string', 'string', '15549459959', 'http://a.jpg', 'string', NULL, 1, '2019-05-21 22:23:59', '2019-05-21 22:23:59');
INSERT INTO `user` VALUES (11, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', NULL, 1, '2019-05-25 22:35:58', '2019-06-19 20:53:30');
INSERT INTO `user` VALUES (12, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', NULL, 1, '2019-08-07 16:01:40', '2019-08-07 16:01:40');
INSERT INTO `user` VALUES (13, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', NULL, 1, '2019-08-07 16:02:11', '2019-08-07 16:02:11');
INSERT INTO `user` VALUES (14, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', NULL, 1, '2019-08-07 16:03:08', '2019-08-07 16:03:08');
INSERT INTO `user` VALUES (15, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', NULL, 1, '2019-08-07 16:03:17', '2019-08-07 16:03:17');
INSERT INTO `user` VALUES (62, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', '{\"name\": \"分类名194\", \"createTime\": 1595562213003, \"updateTime\": 1595562213003}', 1, '2020-07-24 11:43:33', '2020-07-24 11:43:33');
INSERT INTO `user` VALUES (63, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', '{\"name\": \"分类名672\", \"createTime\": 1595562213331, \"updateTime\": 1595562213331}', 1, '2020-07-24 11:43:33', '2020-07-24 11:43:33');
INSERT INTO `user` VALUES (64, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', '{\"name\": \"分类名910\", \"createTime\": 1595562213684, \"updateTime\": 1595562213684}', 1, '2020-07-24 11:43:34', '2020-07-24 11:43:34');
INSERT INTO `user` VALUES (65, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', '{\"name\": \"分类名529\", \"createTime\": 1595562214033, \"updateTime\": 1595562214033}', 1, '2020-07-24 11:43:34', '2020-07-24 11:43:34');
INSERT INTO `user` VALUES (66, 'lufei', '路飞', '18888888888', 'http://a.jpg', '123456', '{\"id\": 887, \"name\": \"分类名583\"}', 1, '2020-07-24 11:52:57', '2020-07-24 11:52:57');
INSERT INTO `user` VALUES (67, '', '王路飞', '', '', '', '{\"name\": \"海贼\", \"path\": \"1,2,3\", \"image\": \"http://wanglufei.img\"}', 1, '2020-07-24 14:11:05', '2020-07-24 14:11:05');
INSERT INTO `user` VALUES (68, '', '王路飞', '', '', '', '{\"name\": \"海贼\", \"path\": \"1,2,3\", \"image\": \"http://wanglufei.img\"}', 1, '2020-07-24 14:11:15', '2020-07-24 14:11:15');
INSERT INTO `user` VALUES (69, '', '王路飞', '', '', '', '{\"name\": \"海贼\", \"path\": \"1,2,3\", \"image\": \"http://wanglufei.img\"}', 1, '2020-07-24 14:15:04', '2020-07-24 14:15:04');

SET FOREIGN_KEY_CHECKS = 1;
