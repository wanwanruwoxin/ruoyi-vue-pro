package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.mysql.DsRewardRuleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DsRewardRuleServiceImpl implements DsRewardRuleService {

    @Resource
    private DsRewardRuleMapper dsRewardRuleMapper;
}
