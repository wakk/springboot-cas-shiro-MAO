/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50735
Source Host           : xxxxxxxxx:3310
Source Database       : db_sso

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2022-01-12 00:44:14
*/
CREATE database if NOT EXISTS `db_sso` default character set utf8 collate utf8_bin;
use `db_sso`;

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for blog_blog
-- ----------------------------
DROP TABLE IF EXISTS `blog_blog`;
CREATE TABLE `blog_blog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '博客标题',
  `description` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '博客描述',
  `content` longtext COLLATE utf8_bin COMMENT '博客内容',
  `collect_num` int(11) NOT NULL DEFAULT '0' COMMENT '博客被收藏次数',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `created` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `id` (`id`,`title`),
  CONSTRAINT `blog_blog_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `cas_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for blog_favorite
-- ----------------------------
DROP TABLE IF EXISTS `blog_favorite`;
CREATE TABLE `blog_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `num` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `blog_favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `cas_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for blog_favoriteinfo
-- ----------------------------
DROP TABLE IF EXISTS `blog_favoriteinfo`;
CREATE TABLE `blog_favoriteinfo` (
  `favorite_id` bigint(20) NOT NULL,
  `blog_id` bigint(20) NOT NULL,
  `blog_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`favorite_id`,`blog_id`),
  KEY `blog_id` (`blog_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `blog_favoriteinfo_ibfk_2` FOREIGN KEY (`blog_id`) REFERENCES `blog_blog` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blog_favoriteinfo_ibfk_3` FOREIGN KEY (`favorite_id`) REFERENCES `blog_favorite` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blog_favoriteinfo_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `cas_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for blog_folder
-- ----------------------------
DROP TABLE IF EXISTS `blog_folder`;
CREATE TABLE `blog_folder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `blog_folder_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `cas_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for blog_folderinfo
-- ----------------------------
DROP TABLE IF EXISTS `blog_folderinfo`;
CREATE TABLE `blog_folderinfo` (
  `folder_id` bigint(20) NOT NULL,
  `blog_id` bigint(20) NOT NULL,
  `blog_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`folder_id`,`blog_id`),
  KEY `blog_id` (`blog_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `blog_folderinfo_ibfk_1` FOREIGN KEY (`blog_id`) REFERENCES `blog_blog` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blog_folderinfo_ibfk_2` FOREIGN KEY (`folder_id`) REFERENCES `blog_folder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blog_folderinfo_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `cas_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for cas_user
-- ----------------------------
DROP TABLE IF EXISTS `cas_user`;
CREATE TABLE `cas_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `sex` int(11) DEFAULT '0',
  `company` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `UK_USERNAME` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
DROP TRIGGER IF EXISTS `tr_blog`;
DELIMITER ;;
CREATE TRIGGER `tr_blog` AFTER UPDATE ON `blog_blog` FOR EACH ROW begin
IF((old.title!= new.title) or (old.title IS NULL && new.title IS NOT NULL))
THEN
update blog_favoriteinfo set blog_name=new.title where blog_id = old.id;
update blog_folderinfo set blog_name=new.title where blog_id = old.id;
END IF;
END
;;
DELIMITER ;
