package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsOrderCommissionSplit;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccount;

import java.time.LocalDateTime;
import java.util.List;

public interface DsPlatformAccountService {

    DsPlatformAccount getOrCreateMainAccount();

    void recordCommissionIncome(Long orderId, String orderNo, LocalDateTime occurredAt, List<DsOrderCommissionSplit> splits);
}
