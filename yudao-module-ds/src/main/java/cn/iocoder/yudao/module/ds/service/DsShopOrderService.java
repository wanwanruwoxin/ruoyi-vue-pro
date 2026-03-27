package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;

import java.util.List;

public interface DsShopOrderService {

    DsShopOrder createAndPayOrder(Long uid, AppDsShopOrderCreateReqVO reqVO);

    List<DsShopOrder> getUserOrders(Long uid);

    List<DsShopOrderItem> getUserOrderItems(Long uid);
}
