DROP TABLE IF EXISTS `dynamic_synonym_rule`;
CREATE TABLE `dynamic_synonym_rule` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '同义词',
  `updatetime` datetime(0) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
-- insert sample records
-- ----------------------------
INSERT INTO `dynamic_synonym_rule`(word, updatetime) VALUES ('阿迪, 阿迪达斯, adidasi => Adidas', now());
INSERT INTO `dynamic_synonym_rule`(word, updatetime) VALUES ('Nike, 耐克, naike', now());
INSERT INTO `dynamic_synonym_rule`(word, updatetime) VALUES ('王者荣耀, LOL, 英雄联盟, 毒害小学生', now());

