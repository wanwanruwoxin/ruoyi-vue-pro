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
    private static final String INVITER_LEVEL_1 = "LEVEL_1";
    private static final String INVITER_LEVEL_2 = "LEVEL_2";
    private static final String TEAM_LEADER_LEVEL3_NEAREST = "TEAM_LEADER_LEVEL3_NEAREST";
    private static final String TEAM_LEADER_LEVEL3_UPPER = "TEAM_LEADER_LEVEL3_UPPER";
    private static final String SHAREHOLDER_POOL = "SHAREHOLDER_POOL";

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
    public DsRewardRule getMembershipInviteRewardRuleByLevel(Integer relationLevel) {
        initDefaultRulesIfAbsent();
        LocalDateTime now = LocalDateTime.now();
        String inviterLevel = relationLevel != null && relationLevel == 2 ? INVITER_LEVEL_2 : INVITER_LEVEL_1;
        DsRewardRule rule = dsRewardRuleMapper.selectActiveRuleByTriggerEventAndInviterLevel(
                MEMBERSHIP_ORDER_PAID_NORMAL.getCode(), inviterLevel, now);
        if (rule != null) {
            return rule;
        }
        return dsRewardRuleMapper.selectActiveRuleByTriggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode(), now);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DsRewardRule getMembershipInviteRewardRuleByInviterLevel(String inviterLevel) {
        initDefaultRulesIfAbsent();
        return dsRewardRuleMapper.selectActiveRuleByTriggerEventAndInviterLevel(
                MEMBERSHIP_ORDER_PAID_NORMAL.getCode(), inviterLevel, LocalDateTime.now());
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
        DsRewardRule existed = dsRewardRuleMapper.selectFirstOne(DsRewardRule::getTriggerEvent, MEMBERSHIP_ORDER_PAID_NORMAL.getCode());
        if (existed != null) {
            initSecondLevelInviteRewardRule();
            initSpecialInviteRewardRule(TEAM_LEADER_LEVEL3_NEAREST, "INVITE_REWARD_TEAM_LEADER_NEAREST_V1",
                    new BigDecimal("0.05"), null);
            initSpecialInviteRewardRule(TEAM_LEADER_LEVEL3_UPPER, "INVITE_REWARD_TEAM_LEADER_UPPER_V1",
                    new BigDecimal("0.02"), null);
            initSpecialInviteRewardRule(SHAREHOLDER_POOL, "INVITE_REWARD_SHAREHOLDER_POOL_V1",
                    new BigDecimal("0.10"), null);
            return;
        }
        DsRewardRule firstLevelRule = DsRewardRule.builder()
                .ruleVersion("INVITE_REWARD_V1")
                .ruleDescription("会员订单支付后，按订单金额的50%奖励给一级邀请人，日封顶300积分")
                .triggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode())
                .rewardRate(new BigDecimal("0.50"))
                .dailyCapPoints(new BigDecimal("300"))
                .applicableInviterLevel(INVITER_LEVEL_1)
                .status(CommonStatusEnum.ENABLE.getStatus())
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(null)
                .build();
        dsRewardRuleMapper.insert(firstLevelRule);
        DsRewardRule secondLevelRule = DsRewardRule.builder()
                .ruleVersion("INVITE_REWARD_LEVEL2_V1")
                .ruleDescription("会员订单支付后，按订单金额的20%奖励给二级邀请人")
                .triggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode())
                .rewardRate(new BigDecimal("0.20"))
                .dailyCapPoints(new BigDecimal("150"))
                .applicableInviterLevel(INVITER_LEVEL_2)
                .status(CommonStatusEnum.ENABLE.getStatus())
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(null)
                .build();
        dsRewardRuleMapper.insert(secondLevelRule);
        initSpecialInviteRewardRule(TEAM_LEADER_LEVEL3_NEAREST, "INVITE_REWARD_TEAM_LEADER_NEAREST_V1",
                new BigDecimal("0.05"), null);
        initSpecialInviteRewardRule(TEAM_LEADER_LEVEL3_UPPER, "INVITE_REWARD_TEAM_LEADER_UPPER_V1",
                new BigDecimal("0.02"), null);
        initSpecialInviteRewardRule(SHAREHOLDER_POOL, "INVITE_REWARD_SHAREHOLDER_POOL_V1",
                new BigDecimal("0.10"), null);
    }

    private void initSecondLevelInviteRewardRule() {
        DsRewardRule secondLevelRule = dsRewardRuleMapper.selectFirstOne(
                DsRewardRule::getTriggerEvent, MEMBERSHIP_ORDER_PAID_NORMAL.getCode(),
                DsRewardRule::getApplicableInviterLevel, INVITER_LEVEL_2);
        if (secondLevelRule != null) {
            return;
        }
        DsRewardRule rule = DsRewardRule.builder()
                .ruleVersion("INVITE_REWARD_LEVEL2_V1")
                .ruleDescription("会员订单支付后，按订单金额的20%奖励给二级邀请人")
                .triggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode())
                .rewardRate(new BigDecimal("0.20"))
                .dailyCapPoints(new BigDecimal("150"))
                .applicableInviterLevel(INVITER_LEVEL_2)
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
                .ruleDescription(resolveConsumeScopeDescription(scopeCode))
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

    private void initSpecialInviteRewardRule(String inviterLevel, String version, BigDecimal rewardRate, BigDecimal dailyCapPoints) {
        DsRewardRule existed = dsRewardRuleMapper.selectFirstOne(
                DsRewardRule::getTriggerEvent, MEMBERSHIP_ORDER_PAID_NORMAL.getCode(),
                DsRewardRule::getApplicableInviterLevel, inviterLevel);
        if (existed != null) {
            return;
        }
        DsRewardRule rule = DsRewardRule.builder()
                .ruleVersion(version)
                .ruleDescription(resolveInviteRewardDescription(inviterLevel))
                .triggerEvent(MEMBERSHIP_ORDER_PAID_NORMAL.getCode())
                .rewardRate(rewardRate)
                .dailyCapPoints(dailyCapPoints)
                .applicableInviterLevel(inviterLevel)
                .status(CommonStatusEnum.ENABLE.getStatus())
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(null)
                .build();
        dsRewardRuleMapper.insert(rule);
    }

    private String resolveConsumeScopeDescription(String scopeCode) {
        if (MEMBERSHIP_ORDER_PAY.getCode().equals(scopeCode)) {
            return "积分消费范围限定为会员订单支付场景";
        }
        if (SHOP_ORDER_PAY.getCode().equals(scopeCode)) {
            return "积分消费范围限定为商城订单支付场景";
        }
        if (POINT_GIFT_SEND.getCode().equals(scopeCode)) {
            return "积分消费范围限定为积分转赠支出场景";
        }
        return "积分消费范围规则";
    }

    private String resolveInviteRewardDescription(String inviterLevel) {
        if (TEAM_LEADER_LEVEL3_NEAREST.equals(inviterLevel)) {
            return "会员订单支付后，按订单金额的5%奖励给最近团队长";
        }
        if (TEAM_LEADER_LEVEL3_UPPER.equals(inviterLevel)) {
            return "会员订单支付后，按订单金额的2%奖励给上级团队长";
        }
        if (SHAREHOLDER_POOL.equals(inviterLevel)) {
            return "会员订单支付后，按订单金额的10%计入股东池";
        }
        return "会员邀请奖励规则";
    }
}
