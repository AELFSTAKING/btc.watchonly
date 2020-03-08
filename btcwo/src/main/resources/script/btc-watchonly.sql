
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for block_cache
-- ----------------------------
DROP TABLE IF EXISTS `block_cache`;
CREATE TABLE `block_cache` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `block_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `height` bigint(20) unsigned NOT NULL,
  `block_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_block_hash` (`block_hash`) USING BTREE,
  KEY `idx_height` (`height`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1773451 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for block_msg
-- ----------------------------
DROP TABLE IF EXISTS `block_msg`;
CREATE TABLE `block_msg` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `height` bigint(20) unsigned NOT NULL,
  `hash` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `msg` mediumtext COLLATE utf8mb4_bin NOT NULL,
  `msg_type` smallint(2) unsigned NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_height` (`height`) USING BTREE COMMENT '高度索引',
  UNIQUE KEY `uniq_hash` (`hash`) USING BTREE COMMENT '区块哈希索引'
) ENGINE=InnoDB AUTO_INCREMENT=1117553 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for btc_block_height
-- ----------------------------
DROP TABLE IF EXISTS `btc_block_height`;
CREATE TABLE `btc_block_height` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `node_latest_block_height` bigint(20) unsigned DEFAULT '0',
  `latest_block_height` bigint(20) unsigned DEFAULT '0',
  `last_call_back_height` bigint(20) unsigned DEFAULT NULL,
  `last_call_back_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for compensation_height
-- ----------------------------
DROP TABLE IF EXISTS `compensation_height`;
CREATE TABLE `compensation_height` (
  `id` bigint(20) NOT NULL,
  `sync_height` bigint(20) unsigned NOT NULL,
  `block_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '对应高度的区块hash',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for msg_queue
-- ----------------------------
DROP TABLE IF EXISTS `msg_queue`;
CREATE TABLE `msg_queue` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `is_callback` tinyint(1) unsigned DEFAULT '0',
  `msg` mediumtext COLLATE utf8mb4_bin NOT NULL,
  `msg_type` smallint(2) unsigned NOT NULL,
  `last_callback_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `height` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_height` (`height`) USING BTREE COMMENT '高度索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for sync_height
-- ----------------------------
DROP TABLE IF EXISTS `sync_height`;
CREATE TABLE `sync_height` (
  `id` bigint(20) unsigned NOT NULL,
  `sync_height` bigint(20) unsigned NOT NULL,
  `block_hash` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '对应高度的区块hash',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
