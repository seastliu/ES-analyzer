/*
 Navicat Premium Data Transfer

 Source Server         : 168.61.2.14 3312
 Source Server Type    : MySQL
 Source Server Version : 100114
 Source Host           : 168.61.2.14 :3312
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 100114
 File Encoding         : 65001

 Date: 20/09/2019 09:33:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ik_ext
-- ----------------------------
DROP TABLE IF EXISTS `ik_ext`;
CREATE TABLE `ik_ext`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `word` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展词',
  `updatetime` datetime(0) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
