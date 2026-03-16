package cn.iocoder.yudao.module.stock.controller.admin;

import cn.iocoder.yudao.module.stock.service.StockHoldingBalanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 持仓余额")
@RestController
@RequestMapping("/stock/holding-balance")
@Validated
public class StockHoldingBalanceController {

    @Resource
    private StockHoldingBalanceService stockHoldingBalanceService;
}
