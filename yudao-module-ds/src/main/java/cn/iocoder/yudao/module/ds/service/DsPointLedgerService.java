package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DsPointLedgerService {

    void createLedger(DsPointLedger ledger);

    BigDecimal sumPointsByUidAndBizTypeBetween(Long uid, String bizType, LocalDateTime startTime, LocalDateTime endTime);
}
