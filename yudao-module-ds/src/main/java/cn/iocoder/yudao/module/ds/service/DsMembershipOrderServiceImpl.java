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
        if (!NORMAL.getCode().equals(plan.getPlanCode())) {
            return;
        }
        if (order.getPayableAmount().compareTo(new BigDecimal("199")) != 0) {
            return;
        }
        rewardForRelationLevel(inviteeUid, order, paidAt, RELATION_LEVEL_1);
        rewardForRelationLevel(inviteeUid, order, paidAt, RELATION_LEVEL_2);
    }

    private void rewardForRelationLevel(Long inviteeUid, DsMembershipOrder order, LocalDateTime paidAt, int relationLevel) {
        Long inviterId = dsInviteRelationService.getInviterIdByInviteeIdAndLevel(inviteeUid, relationLevel);
        if (inviterId == null) {
            return;
        }
        DsRewardRule rule = dsRewardRuleService.getMembershipInviteRewardRuleByLevel(relationLevel);
        if (rule == null || rule.getRewardRate() == null || rule.getRewardRate().signum() <= 0) {
            return;
        }
        BigDecimal rewardPoints = order.getPayableAmount().multiply(rule.getRewardRate()).setScale(2, RoundingMode.HALF_UP);
        DsMembershipAccount inviterAccount = dsMembershipAccountService.getAccount(inviterId);
        boolean advancedMember = ADVANCED.getCode().equals(inviterAccount.getCurrentPlanCode());
        if (!advancedMember && rule.getDailyCapPoints() != null) {
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
        String rewardBizNo = relationLevel == RELATION_LEVEL_1 ? order.getOrderNo() : order.getOrderNo() + "-L2";
        dsPointAccountService.earnPoints(inviterId, rewardPoints, INVITE_MEMBERSHIP_REWARD.getCode(),
                rewardBizNo, inviteeUid, rule.getRuleVersion(), paidAt);
    }
}
