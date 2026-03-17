package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import cn.iocoder.yudao.module.ds.dal.mysql.DsRewardRuleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.MEMBERSHIP_ORDER_PAY;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.POINT_GIFT_SEND;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.SHOP_ORDER_PAY;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.RewardTriggerEvent.MEMBERSHIP_ORDER_PAID_NORMAL;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.RewardTriggerEvent.POINT_CONSUME_SCOPE;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.POINT_CONSUME_SCOPE_DISABLED;

@Service
@Validated
public class DsRewardRuleServiceImpl implements DsRewardRuleService {

    @Resource
    private DsRewardRuleMapper dsRewardRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DsRewardRule getMembershipInviteRewardRule() {
        initDefaultRulesIfAbsent();
        LocalDateTime now = LocalDateTime.now();
        return dsRewardRuleMapper.selectActiveRuleByTriggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode(), now);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void validatePointConsumeScope(String bizType) {
        initDefaultRulesIfAbsent();
        LocalDateTime now = LocalDateTime.now();
        DsRewardRule rule = dsRewardRuleMapper.selectActiveScopeRule(POINT_CONSUME_SCOPE.getCode(), bizType, now);
        if (rule == null) {
            throw exception(POINT_CONSUME_SCOPE_DISABLED);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void initDefaultRulesIfAbsent() {
        initInviteRewardRule();
        initConsumeScopeRule(MEMBERSHIP_ORDER_PAY.getCode(), "SCOPE_MEMBERSHIP_V1");
        initConsumeScopeRule(SHOP_ORDER_PAY.getCode(), "SCOPE_SHOP_V1");
        initConsumeScopeRule(POINT_GIFT_SEND.getCode(), "SCOPE_GIFT_V1");
    }

    private void initInviteRewardRule() {
        DsRewardRule existed = dsRewardRuleMapper.selectOne(DsRewardRule::getTriggerEvent, MEMBERSHIP_ORDER_PAID_NORMAL.getCode());
        if (existed != null) {
            return;
        }
        DsRewardRule rule = DsRewardRule.builder()
                .ruleVersion("INVITE_REWARD_V1")
                .triggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode())
                .rewardRate(new BigDecimal("0.50"))
                .dailyCapPoints(new BigDecimal("300"))
                .applicableInviterLevel("ALL")
                .status(CommonStatusEnum.ENABLE.getStatus())
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(null)
                .build();
        dsRewardRuleMapper.insert(rule);
    }

    private void initConsumeScopeRule(String scopeCode, String version) {
        DsRewardRule existed = dsRewardRuleMapper.selectOne(DsRewardRule::getTriggerEvent, POINT_CONSUME_SCOPE.getCode(),
                DsRewardRule::getApplicableInviterLevel, scopeCode);
        if (existed != null) {
            return;
        }
        DsRewardRule rule = DsRewardRule.builder()
                .ruleVersion(version)
                .triggerEvent(POINT_CONSUME_SCOPE.getCode())
                .rewardRate(BigDecimal.ONE)
                .dailyCapPoints(null)
                .applicableInviterLevel(scopeCode)
                .status(CommonStatusEnum.ENABLE.getStatus())
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(null)
                .build();
        dsRewardRuleMapper.insert(rule);
    }
}
