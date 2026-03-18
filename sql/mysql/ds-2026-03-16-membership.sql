SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS `ds_membership_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `plan_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price_amount` decimal(10, 2) NOT NULL,
  `duration_days` int NOT NULL,
  `status` tinyint NOT NULL DEFAULT 0,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_membership_plan_code` (`plan_code`) USING BTREE,
  KEY `idx_ds_membership_plan_status` (`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_membership_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NOT NULL,
  `current_plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `member_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNOPENED',
  `effective_time` datetime NULL DEFAULT NULL,
  `expire_time` datetime NULL DEFAULT NULL,
  `team_leader` tinyint NOT NULL DEFAULT 0,
  `shareholder` tinyint NOT NULL DEFAULT 0,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_membership_account_uid` (`uid`) USING BTREE,
  KEY `idx_ds_membership_account_status` (`member_status`) USING BTREE,
  KEY `idx_ds_membership_account_shareholder_status` (`shareholder`, `member_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_membership_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uid` bigint NOT NULL,
  `plan_id` bigint NOT NULL,
  `payable_amount` decimal(10, 2) NOT NULL,
  `pay_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `paid_at` datetime NULL DEFAULT NULL,
  `refund_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_membership_order_no` (`order_no`) USING BTREE,
  KEY `idx_ds_membership_order_uid` (`uid`) USING BTREE,
  KEY `idx_ds_membership_order_uid_status` (`uid`, `pay_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_shop` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NOT NULL,
  `shop_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `intro` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `contact_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ship_province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ship_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ship_district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ship_detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort` int NOT NULL DEFAULT 0,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_shop_uid` (`uid`) USING BTREE,
  KEY `idx_ds_shop_name` (`shop_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shop_id` bigint NOT NULL,
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price_amount` decimal(10, 2) NOT NULL,
  `stock` int NOT NULL DEFAULT 0,
  `detail_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `video_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `sale_status` tinyint NOT NULL DEFAULT 0,
  `sort` int NOT NULL DEFAULT 0,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_shop_id` (`shop_id`) USING BTREE,
  KEY `idx_ds_product_sale_status` (`sale_status`) USING BTREE,
  KEY `idx_ds_product_sort` (`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'current_plan_code'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `current_plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'team_leader'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `team_leader` tinyint NOT NULL DEFAULT 0 AFTER `expire_time`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'shareholder'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `shareholder` tinyint NOT NULL DEFAULT 0 AFTER `team_leader`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'member_status'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `member_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''UNOPENED''',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'effective_time'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `effective_time` datetime NULL DEFAULT NULL',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_account'
    AND COLUMN_NAME = 'expire_time'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_account` ADD COLUMN `expire_time` datetime NULL DEFAULT NULL',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_membership_order'
    AND COLUMN_NAME = 'refund_status'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_membership_order` ADD COLUMN `refund_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''NONE''',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT INTO `ds_membership_plan` (`plan_code`, `plan_name`, `price_amount`, `duration_days`, `status`, `creator`, `updater`, `tenant_id`)
VALUES ('NORMAL', '普通会员', 199.00, 365, 0, '', '', 0),
       ('ADVANCED', '高级会员', 1990.00, 365, 0, '', '', 0)
ON DUPLICATE KEY UPDATE
  `plan_name` = VALUES(`plan_name`),
  `price_amount` = VALUES(`price_amount`),
  `duration_days` = VALUES(`duration_days`),
  `status` = VALUES(`status`),
  `updater` = VALUES(`updater`),
  `tenant_id` = VALUES(`tenant_id`);

CREATE TABLE IF NOT EXISTS `ds_point_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NOT NULL,
  `available_points` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `frozen_points` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `total_earned_points` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `total_spent_points` decimal(12, 2) NOT NULL DEFAULT 0.00,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_point_account_uid` (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_point_ledger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NOT NULL,
  `change_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `points` decimal(12, 2) NOT NULL,
  `balance_after` decimal(12, 2) NOT NULL,
  `biz_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `biz_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `source_uid` bigint NULL DEFAULT NULL,
  `reward_rule_version` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `occurred_at` datetime NOT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_point_ledger_uid_biz_time` (`uid`, `biz_type`, `occurred_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_reward_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_version` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `trigger_event` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `reward_rate` decimal(10, 4) NOT NULL DEFAULT 0.0000,
  `daily_cap_points` decimal(12, 2) NULL DEFAULT NULL,
  `applicable_inviter_level` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT 0,
  `effective_from` datetime NULL DEFAULT NULL,
  `effective_to` datetime NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_ds_reward_rule_version` (`rule_version`) USING BTREE,
  KEY `idx_ds_reward_rule_trigger_status` (`trigger_event`, `status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'uid'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `uid` bigint NOT NULL AFTER `id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'change_type'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `change_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL AFTER `uid`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'points'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `points` decimal(12, 2) NOT NULL AFTER `change_type`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'balance_after'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `balance_after` decimal(12, 2) NOT NULL AFTER `points`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'biz_type'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `biz_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL AFTER `balance_after`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'biz_no'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `biz_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `biz_type`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'source_uid'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `source_uid` bigint NULL DEFAULT NULL AFTER `biz_no`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'reward_rule_version'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `reward_rule_version` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `source_uid`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_point_ledger'
    AND COLUMN_NAME = 'occurred_at'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_point_ledger` ADD COLUMN `occurred_at` datetime NOT NULL AFTER `reward_rule_version`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT INTO `ds_reward_rule` (`rule_version`, `trigger_event`, `reward_rate`, `daily_cap_points`, `applicable_inviter_level`, `status`, `effective_from`, `effective_to`, `creator`, `updater`, `tenant_id`)
VALUES ('INVITE_REWARD_V1', 'MEMBERSHIP_ORDER_PAID_NORMAL', 0.5000, 300.00, 'ALL', 0, NOW(), NULL, '', '', 0),
       ('SCOPE_MEMBERSHIP_V1', 'POINT_CONSUME_SCOPE', 1.0000, NULL, 'MEMBERSHIP_ORDER_PAY', 0, NOW(), NULL, '', '', 0),
       ('SCOPE_SHOP_V1', 'POINT_CONSUME_SCOPE', 1.0000, NULL, 'SHOP_ORDER_PAY', 0, NOW(), NULL, '', '', 0)
ON DUPLICATE KEY UPDATE
  `trigger_event` = VALUES(`trigger_event`),
  `reward_rate` = VALUES(`reward_rate`),
  `daily_cap_points` = VALUES(`daily_cap_points`),
  `applicable_inviter_level` = VALUES(`applicable_inviter_level`),
  `status` = VALUES(`status`),
  `effective_from` = VALUES(`effective_from`),
  `effective_to` = VALUES(`effective_to`),
  `updater` = VALUES(`updater`),
  `tenant_id` = VALUES(`tenant_id`);

SET FOREIGN_KEY_CHECKS = 1;
