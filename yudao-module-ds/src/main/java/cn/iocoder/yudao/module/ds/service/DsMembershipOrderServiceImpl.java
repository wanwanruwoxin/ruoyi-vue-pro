package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.CLOSED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PAID;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PENDING;
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
        LocalDateTime paidAt = LocalDateTime.now();
        order.setPayStatus(PAID.getCode());
        order.setPaidAt(paidAt);
        dsMembershipOrderMapper.updateById(order);
        DsMembershipPlan plan = dsMembershipPlanService.getPlan(order.getPlanId());
        dsMembershipAccountService.activateMembership(uid, plan, paidAt);
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
}
