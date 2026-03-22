package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;

import java.time.YearMonth;

public interface DsShareholderPoolService {

    void recordMembershipOrderProfitToPool(DsMembershipOrder order);

    void recordShopOrderProfitToPool(DsShopOrder order);

    void settleMonthlyDividend(YearMonth settleMonth);
}
