package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsTeamConfig;

import java.util.List;
import java.util.Map;

public interface DsTeamConfigService {

    PageResult<DsTeamConfig> getPage(PageParam pageParam, String configKey, String configName,
                                     String configGroup, Integer status);

    DsTeamConfig get(Long id);

    Long create(DsTeamConfig config);

    void update(DsTeamConfig config);

    void delete(Long id);

    Map<String, String> getConfigValueMap(List<String> configKeys);
}
