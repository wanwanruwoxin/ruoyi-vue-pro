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
  `status` tinyint NOT NULL DEFAULT 0,
  `audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `audit_admin_user_id` bigint NULL DEFAULT NULL,
  `backend_admin_user_id` bigint NULL DEFAULT NULL,
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
  `category_id` bigint NULL DEFAULT NULL,
  `brand_id` bigint NULL DEFAULT NULL,
  `product_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `introduction` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `slider_pic_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `price_amount` decimal(10, 2) NOT NULL,
  `market_price` decimal(10, 2) NULL DEFAULT NULL,
  `cost_price` decimal(10, 2) NULL DEFAULT NULL,
  `stock` int NOT NULL DEFAULT 0,
  `detail_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `video_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `spec_type` tinyint NOT NULL DEFAULT 0,
  `delivery_types` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `delivery_template_id` bigint NULL DEFAULT NULL,
  `give_integral` int NOT NULL DEFAULT 0,
  `sub_commission_type` tinyint NULL DEFAULT NULL,
  `sale_status` tinyint NOT NULL DEFAULT 0,
  `sort` int NOT NULL DEFAULT 0,
  `sales_count` int NOT NULL DEFAULT 0,
  `virtual_sales_count` int NOT NULL DEFAULT 0,
  `browse_count` int NOT NULL DEFAULT 0,
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
    AND TABLE_NAME = 'ds_shop'
    AND COLUMN_NAME = 'status'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop` ADD COLUMN `status` tinyint NOT NULL DEFAULT 0 AFTER `ship_detail_address`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop'
    AND COLUMN_NAME = 'audit_remark'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop` ADD COLUMN `audit_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `status`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop'
    AND COLUMN_NAME = 'audit_admin_user_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop` ADD COLUMN `audit_admin_user_id` bigint NULL DEFAULT NULL AFTER `audit_remark`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop'
    AND COLUMN_NAME = 'backend_admin_user_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop` ADD COLUMN `backend_admin_user_id` bigint NULL DEFAULT NULL AFTER `audit_admin_user_id`',
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
  `rule_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `trigger_event` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `applicable_plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
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
    AND TABLE_NAME = 'ds_reward_rule'
    AND COLUMN_NAME = 'rule_description'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_reward_rule` ADD COLUMN `rule_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `rule_version`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_reward_rule'
    AND COLUMN_NAME = 'applicable_plan_code'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_reward_rule` ADD COLUMN `applicable_plan_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `trigger_event`',
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

INSERT INTO `ds_reward_rule` (`rule_version`, `rule_description`, `trigger_event`, `applicable_plan_code`, `reward_rate`, `daily_cap_points`, `applicable_inviter_level`, `status`, `effective_from`, `effective_to`, `creator`, `updater`, `tenant_id`)
VALUES ('INVITE_REWARD_V1', '会员订单支付后，按订单金额的50%奖励给支付会员的直接邀请人（一级），日封顶300积分', 'MEMBERSHIP_ORDER_PAID_NORMAL', 'NORMAL', 0.5000, 300.00, 'LEVEL_1', 0, NOW(), NULL, '', '', 0),
       ('INVITE_REWARD_LEVEL1_ADVANCED_V1', '会员订单支付后，按订单金额的50%奖励给支付会员的直接邀请人（一级）', 'MEMBERSHIP_ORDER_PAID_NORMAL', 'ADVANCED', 0.5000, NULL, 'LEVEL_1', 0, NOW(), NULL, '', '', 0),
       ('SCOPE_MEMBERSHIP_V1', '积分消费范围限定为会员订单支付场景', 'POINT_CONSUME_SCOPE', NULL, 1.0000, NULL, 'MEMBERSHIP_ORDER_PAY', 0, NOW(), NULL, '', '', 0),
       ('SCOPE_SHOP_V1', '积分消费范围限定为商城订单支付场景', 'POINT_CONSUME_SCOPE', NULL, 1.0000, NULL, 'SHOP_ORDER_PAY', 0, NOW(), NULL, '', '', 0)
ON DUPLICATE KEY UPDATE
  `rule_description` = VALUES(`rule_description`),
  `trigger_event` = VALUES(`trigger_event`),
  `applicable_plan_code` = VALUES(`applicable_plan_code`),
  `reward_rate` = VALUES(`reward_rate`),
  `daily_cap_points` = VALUES(`daily_cap_points`),
  `applicable_inviter_level` = VALUES(`applicable_inviter_level`),
  `status` = VALUES(`status`),
  `effective_from` = VALUES(`effective_from`),
  `effective_to` = VALUES(`effective_to`),
  `updater` = VALUES(`updater`),
  `tenant_id` = VALUES(`tenant_id`);

UPDATE `ds_reward_rule`
SET `rule_description` = CASE `rule_version`
  WHEN 'INVITE_REWARD_V1' THEN '会员订单支付后，按订单金额的50%奖励给支付会员的直接邀请人（一级），日封顶300积分'
  WHEN 'INVITE_REWARD_LEVEL1_ADVANCED_V1' THEN '会员订单支付后，按订单金额的50%奖励给支付会员的直接邀请人（一级）'
  WHEN 'INVITE_REWARD_LEVEL2_V1' THEN '会员订单支付后，按订单金额的20%奖励给支付会员的二级邀请人'
  WHEN 'INVITE_REWARD_TEAM_LEADER_NEAREST_V1' THEN '会员订单支付后，按订单金额的5%奖励给最近团队长'
  WHEN 'INVITE_REWARD_TEAM_LEADER_UPPER_V1' THEN '会员订单支付后，按订单金额的2%奖励给上级团队长'
  WHEN 'INVITE_REWARD_SHAREHOLDER_POOL_V1' THEN '会员订单支付后，按订单金额的10%计入股东池'
  WHEN 'SCOPE_MEMBERSHIP_V1' THEN '积分消费范围限定为会员订单支付场景'
  WHEN 'SCOPE_SHOP_V1' THEN '积分消费范围限定为商城订单支付场景'
  WHEN 'SCOPE_GIFT_V1' THEN '积分消费范围限定为积分转赠支出场景'
  ELSE `rule_description`
END;

UPDATE `ds_reward_rule`
SET `applicable_inviter_level` = 'LEVEL_1'
WHERE `rule_version` = 'INVITE_REWARD_V1'
  AND (`applicable_inviter_level` IS NULL OR `applicable_inviter_level` <> 'LEVEL_1');

UPDATE `ds_reward_rule`
SET `applicable_plan_code` = CASE `rule_version`
  WHEN 'INVITE_REWARD_V1' THEN 'NORMAL'
  WHEN 'INVITE_REWARD_LEVEL1_ADVANCED_V1' THEN 'ADVANCED'
  WHEN 'INVITE_REWARD_LEVEL2_V1' THEN 'ADVANCED'
  WHEN 'INVITE_REWARD_TEAM_LEADER_NEAREST_V1' THEN 'ADVANCED'
  WHEN 'INVITE_REWARD_TEAM_LEADER_UPPER_V1' THEN 'ADVANCED'
  WHEN 'INVITE_REWARD_SHAREHOLDER_POOL_V1' THEN 'ADVANCED'
  ELSE `applicable_plan_code`
END
WHERE `trigger_event` = 'MEMBERSHIP_ORDER_PAID_NORMAL';

UPDATE `ds_reward_rule`
SET `daily_cap_points` = NULL
WHERE `rule_version` = 'INVITE_REWARD_LEVEL1_ADVANCED_V1';

CREATE TABLE IF NOT EXISTS `ds_product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT 0,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sort` int NOT NULL DEFAULT 0,
  `status` tinyint NOT NULL DEFAULT 0,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_category_parent` (`parent_id`) USING BTREE,
  KEY `idx_ds_product_category_status` (`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_product_property` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_property_name` (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_product_property_value` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `property_id` bigint NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_property_value_property` (`property_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_product_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `user_nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `anonymous` tinyint NOT NULL DEFAULT 0,
  `order_id` bigint NULL DEFAULT NULL,
  `order_item_id` bigint NULL DEFAULT NULL,
  `spu_id` bigint NOT NULL,
  `spu_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sku_id` bigint NULL DEFAULT NULL,
  `sku_pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sku_properties_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `visible` tinyint NOT NULL DEFAULT 1,
  `scores` tinyint NOT NULL DEFAULT 5,
  `description_scores` tinyint NOT NULL DEFAULT 5,
  `benefit_scores` tinyint NOT NULL DEFAULT 5,
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pic_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `reply_status` tinyint NOT NULL DEFAULT 0,
  `reply_user_id` bigint NULL DEFAULT NULL,
  `reply_content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `reply_time` datetime NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_comment_spu` (`spu_id`) USING BTREE,
  KEY `idx_ds_product_comment_user` (`user_id`) USING BTREE,
  KEY `idx_ds_product_comment_visible` (`visible`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ds_product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `spu_id` bigint NOT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `properties_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `price_amount` decimal(10, 2) NOT NULL DEFAULT 0.01,
  `market_price` decimal(10, 2) NULL DEFAULT NULL,
  `cost_price` decimal(10, 2) NULL DEFAULT NULL,
  `bar_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `stock` int NOT NULL DEFAULT 0,
  `weight` double NULL DEFAULT NULL,
  `volume` double NULL DEFAULT NULL,
  `first_brokerage_price` int NOT NULL DEFAULT 0,
  `second_brokerage_price` int NOT NULL DEFAULT 0,
  `sales_count` int NOT NULL DEFAULT 0,
  `sale_time` datetime NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_ds_product_sku_spu` (`spu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'category_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `category_id` bigint NULL DEFAULT NULL AFTER `shop_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'keyword'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `keyword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `product_name`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'introduction'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `introduction` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `keyword`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'pic_url'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `introduction`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'slider_pic_urls'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `slider_pic_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `pic_url`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'market_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `market_price` decimal(10, 2) NULL DEFAULT NULL AFTER `price_amount`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'cost_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `cost_price` decimal(10, 2) NULL DEFAULT NULL AFTER `market_price`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'spec_type'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `spec_type` tinyint NOT NULL DEFAULT 0 AFTER `video_urls`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'give_integral'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `give_integral` int NOT NULL DEFAULT 0 AFTER `spec_type`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'sales_count'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `sales_count` int NOT NULL DEFAULT 0 AFTER `sort`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'virtual_sales_count'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `virtual_sales_count` int NOT NULL DEFAULT 0 AFTER `sales_count`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'browse_count'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `browse_count` int NOT NULL DEFAULT 0 AFTER `virtual_sales_count`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'spu_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `spu_id` bigint NOT NULL DEFAULT 0 AFTER `id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'properties_json'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `properties_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `spu_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'price_amount'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `price_amount` decimal(10, 2) NOT NULL DEFAULT 0.01 AFTER `properties_json`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'market_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `market_price` decimal(10, 2) NULL DEFAULT NULL AFTER `price_amount`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'cost_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `cost_price` decimal(10, 2) NULL DEFAULT NULL AFTER `market_price`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'bar_code'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `bar_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `cost_price`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'pic_url'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `pic_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `bar_code`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'stock'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `stock` int NOT NULL DEFAULT 0 AFTER `pic_url`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'weight'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `weight` double NULL DEFAULT NULL AFTER `stock`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'volume'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `volume` double NULL DEFAULT NULL AFTER `weight`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'sales_count'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `sales_count` int NOT NULL DEFAULT 0 AFTER `volume`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'sale_time'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `sale_time` datetime NULL DEFAULT NULL AFTER `sales_count`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'brand_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `brand_id` bigint NULL DEFAULT NULL AFTER `category_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'description'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `introduction`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'delivery_types'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `delivery_types` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '''' AFTER `spec_type`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'delivery_template_id'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `delivery_template_id` bigint NULL DEFAULT NULL AFTER `delivery_types`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product'
    AND COLUMN_NAME = 'sub_commission_type'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product` ADD COLUMN `sub_commission_type` tinyint NULL DEFAULT NULL AFTER `give_integral`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'name'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `spu_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'first_brokerage_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `first_brokerage_price` int NOT NULL DEFAULT 0 AFTER `volume`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_product_sku'
    AND COLUMN_NAME = 'second_brokerage_price'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_product_sku` ADD COLUMN `second_brokerage_price` int NOT NULL DEFAULT 0 AFTER `first_brokerage_price`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;
