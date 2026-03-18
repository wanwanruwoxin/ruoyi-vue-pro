package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private static final int RELATION_LEVEL_1 = 1;
    private static final int RELATION_LEVEL_2 = 2;
    private static final int RELATION_LEVEL_3 = 3;
    private static final int TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD = 10;
    private static final String TEAM_LEADER_LEVEL3_NEAREST = "TEAM_LEADER_LEVEL3_NEAREST";
    private static final String TEAM_LEADER_LEVEL3_UPPER = "TEAM_LEADER_LEVEL3_UPPER";
    private static final String SHAREHOLDER_POOL = "SHAREHOLDER_POOL";

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
        if (NORMAL.getCode().equals(plan.getPlanCode())) {
            rewardForRelationLevel(inviteeUid, order, paidAt, RELATION_LEVEL_1);
            return;
        }
        if (!ADVANCED.getCode().equals(plan.getPlanCode())) {
            return;
        }
        rewardForRelationLevel(inviteeUid, order, paidAt, RELATION_LEVEL_1);
        rewardForRelationLevel(inviteeUid, order, paidAt, RELATION_LEVEL_2);
        upgradeTeamLeaderIfQualified(inviteeUid);
        rewardForTeamLeaderLevel3(inviteeUid, order, paidAt);
        rewardForShareholderPool(order, inviteeUid, paidAt);
    }

    private void rewardForRelationLevel(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt, int relationLevel) {
        Long inviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, relationLevel);
        if (inviterId == null) {
            return;
        }
        DsRewardRule rule = dsRewardRuleService.getMembershipInviteRewardRuleByLevel(relationLevel);
        if (rule == null) {
            return;
        }
        String rewardBizNo = relationLevel == RELATION_LEVEL_1 ? order.getOrderNo() : order.getOrderNo() + "-L2";
        grantInviteReward(inviterId, order.getPayableAmount(), rule, rewardBizNo, inviteeUid, paidAt);
    }

    private void upgradeTeamLeaderIfQualified(Long inviteeUid) {
        Long directInviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, RELATION_LEVEL_1);
        if (directInviterId == null || dsMembershipAccountService.isTeamLeader(directInviterId)) {
            return;
        }
        List<Long> directInviteeIds = dsInviteRelationService.getDirectInviteeIds(directInviterId);
        int advancedCount = 0;
        for (Long directInviteeId : directInviteeIds) {
            DsMembershipAccount inviteeAccount = dsMembershipAccountService.getAccountIfPresent(directInviteeId);
            if (inviteeAccount != null && ADVANCED.getCode().equals(inviteeAccount.getCurrentPlanCode())) {
                advancedCount++;
            }
            if (advancedCount >= TEAM_LEADER_DIRECT_ADVANCED_THRESHOLD) {
                dsMembershipAccountService.markAsTeamLeader(directInviterId);
                return;
            }
        }
    }

    private void rewardForTeamLeaderLevel3(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt) {
        List<Long> ancestors = listAncestorInviters(inviteeUid);
        if (ancestors.size() < RELATION_LEVEL_3) {
            return;
        }
        Long nearestTeamLeaderId = null;
        Long upperTeamLeaderId = null;
        for (int i = RELATION_LEVEL_3 - 1; i < ancestors.size(); i++) {
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
        DsRewardRule nearestRule = dsRewardRuleService.getMembershipInviteRewardRuleByInviterLevel(TEAM_LEADER_LEVEL3_NEAREST);
        if (nearestRule != null) {
            grantInviteReward(nearestTeamLeaderId, order.getPayableAmount(), nearestRule,
                    order.getOrderNo() + "-TLN", inviteeUid, paidAt);
        }
        if (upperTeamLeaderId == null) {
            return;
        }
        DsRewardRule upperRule = dsRewardRuleService.getMembershipInviteRewardRuleByInviterLevel(TEAM_LEADER_LEVEL3_UPPER);
        if (upperRule != null) {
            grantInviteReward(upperTeamLeaderId, order.getPayableAmount(), upperRule,
                    order.getOrderNo() + "-TLU", inviteeUid, paidAt);
        }
    }

    private void rewardForShareholderPool(DsMembershipOrder order, Long inviteeUid, LocalDateTime paidAt) {
        DsRewardRule poolRule = dsRewardRuleService.getMembershipInviteRewardRuleByInviterLevel(SHAREHOLDER_POOL);
        if (poolRule == null || poolRule.getRewardRate() == null || poolRule.getRewardRate().signum() <= 0) {
            return;
        }
        List<Long> shareholderUids = dsMembershipAccountService.listActiveShareholderUids();
        if (shareholderUids.isEmpty()) {
            return;
        }
        BigDecimal poolAmount = order.getPayableAmount().multiply(poolRule.getRewardRate()).setScale(2, RoundingMode.HALF_UP);
        if (poolAmount.signum() <= 0) {
            return;
        }
        BigDecimal average = poolAmount.divide(BigDecimal.valueOf(shareholderUids.size()), 2, RoundingMode.DOWN);
        BigDecimal distributed = average.multiply(BigDecimal.valueOf(shareholderUids.size()));
        BigDecimal remainder = poolAmount.subtract(distributed);
        for (int i = 0; i < shareholderUids.size(); i++) {
            Long shareholderUid = shareholderUids.get(i);
            BigDecimal rewardAmount = i == shareholderUids.size() - 1 ? average.add(remainder) : average;
            if (rewardAmount.signum() <= 0) {
                continue;
            }
            grantRewardPoints(shareholderUid, rewardAmount, poolRule,
                    order.getOrderNo() + "-SP-" + shareholderUid, inviteeUid, paidAt);
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
}
