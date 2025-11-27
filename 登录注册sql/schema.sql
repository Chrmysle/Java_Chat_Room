/*
Navicat MySQL Data Transfer

Source Server         : 111
Source Server Version : 80027
Source Host           : localhost:3306
Source Database       : schema

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2025-11-27 14:52:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sms_codes
-- ----------------------------
DROP TABLE IF EXISTS `sms_codes`;
CREATE TABLE `sms_codes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) NOT NULL,
  `code` varchar(10) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sms_codes
-- ----------------------------
INSERT INTO `sms_codes` VALUES ('16', '15635863031', '475442', '2025-11-25 10:13:24');
INSERT INTO `sms_codes` VALUES ('18', '15635860222', '882415', '2025-11-25 10:14:08');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'admin', 'admin', null);
INSERT INTO `users` VALUES ('6', 'user_68845818', '123', '15635860304');
INSERT INTO `users` VALUES ('7', '18910295006', '123456', null);
