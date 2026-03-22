package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsTeamConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsTeamConfigMapper extends BaseMapperX<DsTeamConfig> {

    default PageResult<DsTeamConfig> selectPage(PageParam pageParam, String configKey, String configName,
                                                String configGroup, Integer status) {
        return selectPage(pageParam, new LambdaQueryWrapperX<DsTeamConfig>()
                .likeIfPresent(DsTeamConfig::getConfigKey, configKey)
                .likeIfPresent(DsTeamConfig::getConfigName, configName)
                .eqIfPresent(DsTeamConfig::getConfigGroup, configGroup)
                .eqIfPresent(DsTeamConfig::getStatus, status)
                .orderByAsc(DsTeamConfig::getSort)
                .orderByAsc(DsTeamConfig::getId));
    }

    default DsTeamConfig selectByConfigKey(String configKey) {
        return selectOne(DsTeamConfig::getConfigKey, configKey);
    }

    default List<DsTeamConfig> selectListByConfigKeys(List<String> configKeys) {
        if (configKeys == null || configKeys.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapperX<DsTeamConfig>()
                .in(DsTeamConfig::getConfigKey, configKeys));
    }
}
