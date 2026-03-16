package cn.iocoder.yudao.module.stock.controller.admin;

import cn.iocoder.yudao.module.stock.service.StockPnlSummaryDailyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 每日盈亏汇总")
@RestController
@RequestMapping("/stock/pnl-summary-daily")
@Validated
public class StockPnlSummaryDailyController {

    @Resource
    private StockPnlSummaryDailyService stockPnlSummaryDailyService;
}
