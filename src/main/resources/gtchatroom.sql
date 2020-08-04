/*
 Navicat MySQL Data Transfer

 Source Server         : javafx
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : gtchatroom

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 26/07/2020 21:17:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createuser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL,
  `iconUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `group_index1`(`groupname`) USING BTREE,
  INDEX `group_index2`(`createuser`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group
-- ----------------------------
INSERT INTO `group` VALUES (1, 'tengxun', 'server', NULL, 'resource/tengXunNews.png');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` tinyint(255) NULL DEFAULT NULL,
  `createtime` datetime(0) NULL DEFAULT NULL,
  `iconUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_index1`(`username`) USING BTREE,
  INDEX `user_index2`(`nickname`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (12, NULL, '123456', 'xiong', 0, '2020-07-15 21:57:15', 'resource/icon.jpeg');
INSERT INTO `user` VALUES (13, NULL, '123456', 'xiong2', 0, '2020-07-15 22:39:36', 'resource/icon.jpeg');
INSERT INTO `user` VALUES (14, NULL, '1', '1', 0, '2020-07-17 23:26:39', 'file:D:\\Progrma\\GtChatClient\\src\\upload\\1_202007250824.png');
INSERT INTO `user` VALUES (15, NULL, '2', '2', 0, '2020-07-18 21:18:01', 'resource/icon.jpeg');

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `groupname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES (1, '1', 'tengxun');
INSERT INTO `user_group` VALUES (2, '2', 'tengxun');

SET FOREIGN_KEY_CHECKS = 1;
