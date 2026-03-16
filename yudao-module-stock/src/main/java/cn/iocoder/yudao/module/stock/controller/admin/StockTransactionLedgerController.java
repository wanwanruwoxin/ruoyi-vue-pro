package cn.iocoder.yudao.module.stock.controller.admin;

import cn.iocoder.yudao.module.stock.service.StockTransactionLedgerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 交易流水")
@RestController
@RequestMapping("/stock/transaction-ledger")
@Validated
public class StockTransactionLedgerController {

    @Resource
    private StockTransactionLedgerService stockTransactionLedgerService;
}
