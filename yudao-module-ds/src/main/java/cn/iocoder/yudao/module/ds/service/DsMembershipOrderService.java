package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;

import java.util.List;

public interface DsMembershipOrderService {

    DsMembershipOrder createOrder(Long uid, Long planId);

    void payOrder(Long uid, String orderNo);

    void closeOrder(Long uid, String orderNo);

    void refundOrder(Long uid, String orderNo);

    List<DsMembershipOrder> getUserOrders(Long uid);
}
