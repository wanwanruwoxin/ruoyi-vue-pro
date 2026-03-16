package cn.iocoder.yudao.module.stock.service;

import cn.iocoder.yudao.module.stock.dal.mysql.StockTransactionLedgerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class StockTransactionLedgerServiceImpl implements StockTransactionLedgerService {

    @Resource
    private StockTransactionLedgerMapper stockTransactionLedgerMapper;
}
