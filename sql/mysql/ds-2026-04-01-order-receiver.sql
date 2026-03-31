SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_name'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `product_summary`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_mobile'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `receiver_name`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_province'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `receiver_mobile`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_city'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `receiver_province`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_district'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `receiver_city`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'ds_shop_order'
    AND COLUMN_NAME = 'receiver_detail_address'
);
SET @sql := IF(@exists = 0,
  'ALTER TABLE `ds_shop_order` ADD COLUMN `receiver_detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL AFTER `receiver_district`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET FOREIGN_KEY_CHECKS = 1;
