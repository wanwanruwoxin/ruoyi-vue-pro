CREATE TABLE IF NOT EXISTS `ds_team_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置键',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `config_value` varchar(255) NOT NULL COMMENT '配置值',
  `value_type` varchar(20) NOT NULL COMMENT '值类型',
  `config_group` varchar(50) NOT NULL COMMENT '配置分组',
  `sort` int NOT NULL DEFAULT '1' COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ds_team_config_tenant_key` (`tenant_id`,`config_key`,`deleted`),
  KEY `idx_ds_team_config_group` (`config_group`),
  KEY `idx_ds_team_config_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='团队奖励配置表';

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'RELATION_LEVEL_1', '一级关系层级值', '1', 'INT', 'TEAM_REWARD', 1, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'RELATION_LEVEL_1' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'RELATION_LEVEL_2', '二级关系层级值', '2', 'INT', 'TEAM_REWARD', 2, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'RELATION_LEVEL_2' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'RELATION_LEVEL_3', '三级关系层级值', '3', 'INT', 'TEAM_REWARD', 3, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'RELATION_LEVEL_3' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD', '团队长升级直推高级人数阈值', '10', 'INT', 'TEAM_REWARD', 4, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'TEAM_LEADER_LEVEL3_NEAREST', '最近团队长奖励层级标识', 'TEAM_LEADER_LEVEL3_NEAREST', 'STRING', 'TEAM_REWARD', 5, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'TEAM_LEADER_LEVEL3_NEAREST' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'TEAM_LEADER_LEVEL3_UPPER', '上级团队长奖励层级标识', 'TEAM_LEADER_LEVEL3_UPPER', 'STRING', 'TEAM_REWARD', 6, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'TEAM_LEADER_LEVEL3_UPPER' AND `deleted` = b'0');

INSERT INTO `ds_team_config` (`config_key`, `config_name`, `config_value`, `value_type`, `config_group`, `sort`, `status`, `remark`, `tenant_id`)
SELECT 'SHAREHOLDER_POOL', '股东池奖励层级标识', 'SHAREHOLDER_POOL', 'STRING', 'TEAM_REWARD', 7, 0, '系统默认初始化', 0
WHERE NOT EXISTS (SELECT 1 FROM `ds_team_config` WHERE `tenant_id` = 0 AND `config_key` = 'SHAREHOLDER_POOL' AND `deleted` = b'0');
