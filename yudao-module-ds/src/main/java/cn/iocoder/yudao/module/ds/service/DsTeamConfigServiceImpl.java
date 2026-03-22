package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsTeamConfig;
import cn.iocoder.yudao.module.ds.dal.mysql.DsTeamConfigMapper;
import cn.iocoder.yudao.module.ds.enums.DsTeamConfigConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class DsTeamConfigServiceImpl implements DsTeamConfigService {

    @Resource
    private DsTeamConfigMapper dsTeamConfigMapper;

    @Override
    public PageResult<DsTeamConfig> getPage(PageParam pageParam, String configKey, String configName,
                                            String configGroup, Integer status) {
        return dsTeamConfigMapper.selectPage(pageParam, configKey, configName, configGroup, status);
    }

    @Override
    public DsTeamConfig get(Long id) {
        return dsTeamConfigMapper.selectById(id);
    }

    @Override
    public Long create(DsTeamConfig config) {
        config.setId(null);
        dsTeamConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    public void update(DsTeamConfig config) {
        dsTeamConfigMapper.updateById(config);
    }

    @Override
    public void delete(Long id) {
        dsTeamConfigMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> getConfigValueMap(List<String> configKeys) {
        initDefaultConfigsIfAbsent();
        List<DsTeamConfig> configs = dsTeamConfigMapper.selectListByConfigKeys(configKeys);
        Map<String, DsTeamConfig> configMap = new HashMap<>();
        for (DsTeamConfig config : configs) {
            configMap.put(config.getConfigKey(), config);
        }
        Map<String, String> defaults = DsTeamConfigConstants.defaultConfigValues();
        Map<String, String> result = new LinkedHashMap<>();
        for (String key : configKeys) {
            DsTeamConfig config = configMap.get(key);
            if (config != null && CommonStatusEnum.ENABLE.getStatus().equals(config.getStatus())
                    && StrUtil.isNotBlank(config.getConfigValue())) {
                result.put(key, config.getConfigValue().trim());
                continue;
            }
            result.put(key, defaults.get(key));
        }
        return result;
    }

    private void initDefaultConfigsIfAbsent() {
        Map<String, String> defaults = DsTeamConfigConstants.defaultConfigValues();
        int sort = 1;
        for (Map.Entry<String, String> entry : defaults.entrySet()) {
            DsTeamConfig existed = dsTeamConfigMapper.selectByConfigKey(entry.getKey());
            if (existed != null) {
                sort++;
                continue;
            }
            DsTeamConfig config = DsTeamConfig.builder()
                    .configKey(entry.getKey())
                    .configName(resolveConfigName(entry.getKey()))
                    .configValue(entry.getValue())
                    .valueType(resolveValueType(entry.getKey()))
                    .configGroup(DsTeamConfigConstants.CONFIG_GROUP_TEAM_REWARD)
                    .sort(sort++)
                    .status(CommonStatusEnum.ENABLE.getStatus())
                    .remark("系统默认初始化")
                    .build();
            dsTeamConfigMapper.insert(config);
        }
    }

    private String resolveValueType(String configKey) {
        if (DsTeamConfigConstants.KEY_RELATION_LEVEL_1.equals(configKey)
                || DsTeamConfigConstants.KEY_RELATION_LEVEL_2.equals(configKey)
                || DsTeamConfigConstants.KEY_RELATION_LEVEL_3.equals(configKey)
                || DsTeamConfigConstants.KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD.equals(configKey)
                || DsTeamConfigConstants.KEY_SHAREHOLDER_POOL_RATE.equals(configKey)) {
            return DsTeamConfigConstants.VALUE_TYPE_INT;
        }
        return DsTeamConfigConstants.VALUE_TYPE_STRING;
    }

    private String resolveConfigName(String configKey) {
        if (DsTeamConfigConstants.KEY_RELATION_LEVEL_1.equals(configKey)) {
            return "一级关系层级值";
        }
        if (DsTeamConfigConstants.KEY_RELATION_LEVEL_2.equals(configKey)) {
            return "二级关系层级值";
        }
        if (DsTeamConfigConstants.KEY_RELATION_LEVEL_3.equals(configKey)) {
            return "三级关系层级值";
        }
        if (DsTeamConfigConstants.KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD.equals(configKey)) {
            return "团队长升级直推高级人数阈值";
        }
        if (DsTeamConfigConstants.KEY_TEAM_LEADER_LEVEL3_NEAREST.equals(configKey)) {
            return "最近团队长奖励层级标识";
        }
        if (DsTeamConfigConstants.KEY_TEAM_LEADER_LEVEL3_UPPER.equals(configKey)) {
            return "上级团队长奖励层级标识";
        }
        if (DsTeamConfigConstants.KEY_SHAREHOLDER_POOL.equals(configKey)) {
            return "股东池奖励层级标识";
        }
        if (DsTeamConfigConstants.KEY_SHAREHOLDER_POOL_RATE.equals(configKey)) {
            return "股东池入池比例(%)";
        }
        return configKey;
    }
}
