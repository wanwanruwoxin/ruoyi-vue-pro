package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

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

    default DsRewardRule selectActiveRuleByTriggerEventAndInviterLevel(String triggerEvent, String inviterLevel, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<DsRewardRule>()
                .eq(DsRewardRule::getTriggerEvent, triggerEvent)
                .eq(DsRewardRule::getApplicableInviterLevel, inviterLevel)
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

    default DsRewardRule selectActiveRuleByTriggerEventAndInviterLevelAndPlanCode(
            String triggerEvent, String inviterLevel, String planCode, LocalDateTime now) {
        return selectOne(new LambdaQueryWrapperX<DsRewardRule>()
                .eq(DsRewardRule::getTriggerEvent, triggerEvent)
                .eq(DsRewardRule::getApplicableInviterLevel, inviterLevel)
                .eq(DsRewardRule::getApplicablePlanCode, planCode)
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

    default List<DsRewardRule> selectActiveInviteRulesByPlanCode(String planCode, LocalDateTime now) {
        return selectList(new LambdaQueryWrapperX<DsRewardRule>()
                .eq(DsRewardRule::getTriggerEvent, "MEMBERSHIP_ORDER_PAID_NORMAL")
                .eq(DsRewardRule::getStatus, 0)
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveFrom)
                        .or()
                        .le(DsRewardRule::getEffectiveFrom, now))
                .and(wrapper -> wrapper.isNull(DsRewardRule::getEffectiveTo)
                        .or()
                        .ge(DsRewardRule::getEffectiveTo, now))
                .and(wrapper -> wrapper.eq(DsRewardRule::getApplicablePlanCode, planCode)
                        .or()
                        .eq(DsRewardRule::getApplicablePlanCode, "ALL"))
                .orderByAsc(DsRewardRule::getId));
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
