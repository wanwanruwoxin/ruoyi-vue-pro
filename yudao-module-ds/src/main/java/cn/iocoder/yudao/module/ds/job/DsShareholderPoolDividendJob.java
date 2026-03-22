package cn.iocoder.yudao.module.ds.job;

import cn.iocoder.yudao.module.ds.service.DsShareholderPoolService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@Slf4j
public class DsShareholderPoolDividendJob {

    @Resource
    private DsShareholderPoolService dsShareholderPoolService;

    @Scheduled(cron = "0 0 2 1 * ?")
    public void settleLastMonthDividend() {
        YearMonth settleMonth = YearMonth.now().minusMonths(1);
        try {
            dsShareholderPoolService.settleMonthlyDividend(settleMonth);
        } catch (Exception ex) {
            log.error("[settleLastMonthDividend][结算股东池分红失败][month={}]", settleMonth, ex);
        }
    }
}
