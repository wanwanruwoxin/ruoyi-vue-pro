package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointLedgerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Validated
public class DsPointLedgerServiceImpl implements DsPointLedgerService {

    @Resource
    private DsPointLedgerMapper dsPointLedgerMapper;

    @Override
    public void createLedger(DsPointLedger ledger) {
        dsPointLedgerMapper.insert(ledger);
    }

    @Override
    public BigDecimal sumPointsByUidAndBizTypeBetween(Long uid, String bizType, LocalDateTime startTime, LocalDateTime endTime) {
        BigDecimal sum = dsPointLedgerMapper.sumPointsByUidAndBizTypeBetween(uid, bizType, startTime, endTime);
        return sum == null ? BigDecimal.ZERO : sum;
    }
}
