package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipOrderMapper;
import cn.iocoder.yudao.module.ds.enums.DsTeamConfigConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.ADVANCED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.NORMAL;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.CLOSED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PAID;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PENDING;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.INVITE_MEMBERSHIP_REWARD;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.MEMBERSHIP_ORDER_PAY;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.REFUNDED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.RefundStatus.NONE;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.RefundStatus.SUCCESS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.MEMBERSHIP_ORDER_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.MEMBERSHIP_ORDER_STATUS_ILLEGAL;

@Service
@Validated
public class DsMembershipOrderServiceImpl implements DsMembershipOrderService {
    @Resource
    private DsMembershipOrderMapper dsMembershipOrderMapper;
    @Resource
    private DsMembershipPlanService dsMembershipPlanService;
    @Resource
    private DsMembershipAccountService dsMembershipAccountService;
    @Resource
    private DsPointAccountService dsPointAccountService;
    @Resource
    private DsInviteRelationService dsInviteRelationService;
    @Resource
    private DsRewardRuleService dsRewardRuleService;
    @Resource
    private DsPointLedgerService dsPointLedgerService;
    @Resource
    private DsTeamConfigService dsTeamConfigService;
    @Resource
    private DsShareholderPoolService dsShareholderPoolService;

    @Override
    public DsMembershipOrder createOrder(Long uid, Long planId) {
        DsMembershipPlan plan = dsMembershipPlanService.getPlan(planId);
        DsMembershipOrder order = DsMembershipOrder.builder()
                .orderNo(generateOrderNo())
                .uid(uid)
                .planId(plan.getId())
                .payableAmount(plan.getPriceAmount())
                .payStatus(PENDING.getCode())
                .refundStatus(NONE.getCode())
                .build();
        dsMembershipOrderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long uid, String orderNo) {
        DsMembershipOrder order = getOrder(uid, orderNo);
        if (!PENDING.getCode().equals(order.getPayStatus())) {
            throw exception(MEMBERSHIP_ORDER_STATUS_ILLEGAL);
        }
        DsMembershipPlan plan = dsMembershipPlanService.getPlan(order.getPlanId());
        dsPointAccountService.spendPoints(uid, order.getPayableAmount(),
                MEMBERSHIP_ORDER_PAY.getCode(), order.getOrderNo(), LocalDateTime.now());
        LocalDateTime paidAt = LocalDateTime.now();
        order.setPayStatus(PAID.getCode());
        order.setPaidAt(paidAt);
        dsMembershipOrderMapper.updateById(order);
        dsMembershipAccountService.activateMembership(uid, plan, paidAt);
        dsShareholderPoolService.recordMembershipOrderProfitToPool(order);
        rewardInviterIfMatched(uid, order, plan, paidAt);
    }

    @Override
    public void closeOrder(Long uid, String orderNo) {
        DsMembershipOrder order = getOrder(uid, orderNo);
        if (!PENDING.getCode().equals(order.getPayStatus())) {
            throw exception(MEMBERSHIP_ORDER_STATUS_ILLEGAL);
        }
        order.setPayStatus(CLOSED.getCode());
        dsMembershipOrderMapper.updateById(order);
    }

    @Override
    public void refundOrder(Long uid, String orderNo) {
        DsMembershipOrder order = getOrder(uid, orderNo);
        if (!PAID.getCode().equals(order.getPayStatus())) {
            throw exception(MEMBERSHIP_ORDER_STATUS_ILLEGAL);
        }
        order.setPayStatus(REFUNDED.getCode());
        order.setRefundStatus(SUCCESS.getCode());
        dsMembershipOrderMapper.updateById(order);
    }

    @Override
    public List<DsMembershipOrder> getUserOrders(Long uid) {
        return dsMembershipOrderMapper.selectListByUid(uid);
    }

    private DsMembershipOrder getOrder(Long uid, String orderNo) {
        DsMembershipOrder order = dsMembershipOrderMapper.selectByOrderNoAndUid(orderNo, uid);
        if (order == null) {
            throw exception(MEMBERSHIP_ORDER_NOT_EXISTS);
        }
        return order;
    }

    private String generateOrderNo() {
        return "M" + IdUtil.fastSimpleUUID();
    }

    private void rewardInviterIfMatched(Long inviteeUid, DsMembershipOrder order, DsMembershipPlan plan, LocalDateTime paidAt) {
        TeamRewardConfig config = resolveTeamRewardConfig();
        rewardForFirstLevelByInviterMembership(inviteeUid, order, paidAt, config);
        rewardForSecondLevelAdvancedInviter(inviteeUid, order, paidAt, config);
        if (!ADVANCED.getCode().equals(plan.getPlanCode())) {
            return;
        }
        upgradeTeamLeaderIfQualified(inviteeUid, config);
        rewardForTeamLeaderLevel3(inviteeUid, order, paidAt, plan.getPlanCode(), config);
    }

    private void rewardForFirstLevelByInviterMembership(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt,
                                                         TeamRewardConfig config) {
        Long firstLevelInviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, config.relationLevel1);
        if (firstLevelInviterId == null) {
            return;
        }
        DsMembershipAccount firstLevelInviterAccount = dsMembershipAccountService.getAccountIfPresent(firstLevelInviterId);
        if (firstLevelInviterAccount == null) {
            return;
        }
        String inviterPlanCode = firstLevelInviterAccount.getCurrentPlanCode();
        if (!NORMAL.getCode().equals(inviterPlanCode) && !ADVANCED.getCode().equals(inviterPlanCode)) {
            return;
        }
        DsRewardRule rule = dsRewardRuleService.getMembershipInviteRewardRuleByLevel(config.relationLevel1, inviterPlanCode);
        if (rule == null) {
            return;
        }
        grantInviteReward(firstLevelInviterId, order.getPayableAmount(), rule, order.getOrderNo(), inviteeUid, paidAt);
    }

    private void rewardForSecondLevelAdvancedInviter(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt,
                                                     TeamRewardConfig config) {
        Long secondLevelInviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, config.relationLevel2);
        if (secondLevelInviterId == null) {
            return;
        }
        DsMembershipAccount secondLevelInviterAccount = dsMembershipAccountService.getAccountIfPresent(secondLevelInviterId);
        if (secondLevelInviterAccount == null || !ADVANCED.getCode().equals(secondLevelInviterAccount.getCurrentPlanCode())) {
            return;
        }
        DsRewardRule rule = dsRewardRuleService.getMembershipInviteRewardRuleByLevel(config.relationLevel2, ADVANCED.getCode());
        if (rule == null) {
            return;
        }
        grantInviteReward(secondLevelInviterId, order.getPayableAmount(), rule, order.getOrderNo() + "-L2", inviteeUid, paidAt);
    }

    private void upgradeTeamLeaderIfQualified(Long inviteeUid, TeamRewardConfig config) {
        Long directInviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, config.relationLevel1);
        if (directInviterId == null || dsMembershipAccountService.isTeamLeader(directInviterId)) {
            return;
        }
        DsMembershipAccount directInviterAccount = dsMembershipAccountService.getAccountIfPresent(directInviterId);
        if (directInviterAccount == null || !ADVANCED.getCode().equals(directInviterAccount.getCurrentPlanCode())) {
            return;
        }
        List<Long> directInviteeIds = dsInviteRelationService.getDirectInviteeIds(directInviterId);
        int advancedCount = dsMembershipAccountService.countByUidsAndPlanCode(directInviteeIds, ADVANCED.getCode());
        if (advancedCount >= config.teamLeaderDirectAdvancedThreshold) {
            dsMembershipAccountService.markAsTeamLeader(directInviterId);
        }
    }

    private void rewardForTeamLeaderLevel3(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt,
                                           String planCode, TeamRewardConfig config) {
        List<Long> ancestors = listAncestorInviters(inviteeUid);
        if (ancestors.size() < config.relationLevel3) {
            return;
        }
        Long nearestTeamLeaderId = null;
        Long upperTeamLeaderId = null;
        for (int i = config.relationLevel3 - 1; i < ancestors.size(); i++) {
            Long inviterId = ancestors.get(i);
            if (!dsMembershipAccountService.isTeamLeader(inviterId)) {
                continue;
            }
            if (nearestTeamLeaderId == null) {
                nearestTeamLeaderId = inviterId;
                continue;
            }
            upperTeamLeaderId = inviterId;
            break;
        }
        if (nearestTeamLeaderId == null) {
            return;
        }
        DsRewardRule nearestRule = dsRewardRuleService.getMembershipInviteRewardRuleByInviterLevel(
                config.teamLeaderLevel3Nearest, planCode);
        if (nearestRule != null) {
            grantInviteReward(nearestTeamLeaderId, order.getPayableAmount(), nearestRule,
                    order.getOrderNo() + "-TLN", inviteeUid, paidAt);
        }
        if (upperTeamLeaderId == null) {
            return;
        }
        DsRewardRule upperRule = dsRewardRuleService.getMembershipInviteRewardRuleByInviterLevel(
                config.teamLeaderLevel3Upper, planCode);
        if (upperRule != null) {
            grantInviteReward(upperTeamLeaderId, order.getPayableAmount(), upperRule,
                    order.getOrderNo() + "-TLU", inviteeUid, paidAt);
        }
    }

    private List<Long> listAncestorInviters(Long inviteeUid) {
        Long currentInviteeId = inviteeUid;
        List<Long> ancestors = new java.util.ArrayList<>();
        while (true) {
            Long inviterId = dsInviteRelationService.getInviterIdByInviteeId(currentInviteeId);
            if (inviterId == null) {
                break;
            }
            ancestors.add(inviterId);
            currentInviteeId = inviterId;
        }
        return ancestors;
    }

    private void grantInviteReward(Long inviterId, BigDecimal rewardBase, DsRewardRule rule,
                                   String rewardBizNo, Long sourceUid, LocalDateTime paidAt) {
        if (inviterId == null || rule == null || rewardBase == null
                || rule.getRewardRate() == null || rule.getRewardRate().signum() <= 0) {
            return;
        }
        BigDecimal rewardPoints = rewardBase.multiply(rule.getRewardRate()).setScale(2, RoundingMode.HALF_UP);
        grantRewardPoints(inviterId, rewardPoints, rule, rewardBizNo, sourceUid, paidAt);
    }

    private void grantRewardPoints(Long inviterId, BigDecimal rewardPoints, DsRewardRule rule,
                                   String rewardBizNo, Long sourceUid, LocalDateTime paidAt) {
        if (rewardPoints == null || rewardPoints.signum() <= 0) {
            return;
        }
        DsMembershipAccount inviterAccount = dsMembershipAccountService.getAccount(inviterId);
        boolean normalMember = NORMAL.getCode().equals(inviterAccount.getCurrentPlanCode());
        if (normalMember && rule.getDailyCapPoints() != null) {
            LocalDate rewardDate = paidAt.toLocalDate();
            LocalDateTime startTime = rewardDate.atStartOfDay();
            LocalDateTime endTime = rewardDate.plusDays(1).atStartOfDay();
            BigDecimal rewardedToday = dsPointLedgerService.sumPointsByUidAndBizTypeBetween(inviterId,
                    INVITE_MEMBERSHIP_REWARD.getCode(), startTime, endTime);
            BigDecimal remaining = rule.getDailyCapPoints().subtract(rewardedToday);
            if (remaining.signum() <= 0) {
                return;
            }
            if (rewardPoints.compareTo(remaining) > 0) {
                rewardPoints = remaining;
            }
        }
        if (rewardPoints.signum() <= 0) {
            return;
        }
        dsPointAccountService.earnPoints(inviterId, rewardPoints, INVITE_MEMBERSHIP_REWARD.getCode(),
                rewardBizNo, sourceUid, rule.getRuleVersion(), paidAt);
    }

    private TeamRewardConfig resolveTeamRewardConfig() {
        Map<String, String> values = dsTeamConfigService.getConfigValueMap(DsTeamConfigConstants.TEAM_REWARD_KEYS);
        int relationLevel1 = parseInt(values.get(DsTeamConfigConstants.KEY_RELATION_LEVEL_1), 1);
        int relationLevel2 = parseInt(values.get(DsTeamConfigConstants.KEY_RELATION_LEVEL_2), 2);
        int relationLevel3 = parseInt(values.get(DsTeamConfigConstants.KEY_RELATION_LEVEL_3), 3);
        int threshold = parseInt(values.get(DsTeamConfigConstants.KEY_TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD), 10);
        String nearest = parseString(values.get(DsTeamConfigConstants.KEY_TEAM_LEADER_LEVEL3_NEAREST), "TEAM_LEADER_LEVEL3_NEAREST");
        String upper = parseString(values.get(DsTeamConfigConstants.KEY_TEAM_LEADER_LEVEL3_UPPER), "TEAM_LEADER_LEVEL3_UPPER");
        String pool = parseString(values.get(DsTeamConfigConstants.KEY_SHAREHOLDER_POOL), "SHAREHOLDER_POOL");
        return new TeamRewardConfig(relationLevel1, relationLevel2, relationLevel3, threshold, nearest, upper, pool);
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    private String parseString(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }

    private record TeamRewardConfig(int relationLevel1,
                                    int relationLevel2,
                                    int relationLevel3,
                                    int teamLeaderDirectAdvancedThreshold,
                                    String teamLeaderLevel3Nearest,
                                    String teamLeaderLevel3Upper,
                                    String shareholderPool) {
    }
}
