package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface DsRewardRuleMapper extends BaseMapperX<DsRewardRule> {

    default DsRewardRule selectActiveRuleByTriggerEvent(String triggerEvent, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<DsRewardRule>()
                .eq(DsRewardRule::getTriggerEvent, triggerEvent)
                .eq(DsRewardRule::getStatus, 0)
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveFrom)
                        .or()
                        .le(DsRewardRule::getEffectiveFrom, now))
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveTo)
                        .or()
                        .ge(DsRewardRule::getEffectiveTo, now))
                .orderByDesc(DsRewardRule::getId)
                .last("LIMIT 1"));
    }

    default DsRewardRule selectActiveScopeRule(String triggerEvent, String scopeCode, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<DsRewardRule>()
                .eq(DsRewardRule::getTriggerEvent, triggerEvent)
                .eq(DsRewardRule::getApplicableInviterLevel, scopeCode)
                .eq(DsRewardRule::getStatus, 0)
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveFrom)
                        .or()
                        .le(DsRewardRule::getEffectiveFrom, now))
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveTo)
                        .or()
                        .ge(DsRewardRule::getEffectiveTo, now))
                .orderByDesc(DsRewardRule::getId)
                .last("LIMIT 1"));
    }
}
