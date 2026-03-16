package cn.iocoder.yudao.module.stock.service;

import cn.iocoder.yudao.module.stock.dal.mysql.StockInvestmentAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class StockInvestmentAccountServiceImpl implements StockInvestmentAccountService {

    @Resource
    private StockInvestmentAccountMapper stockInvestmentAccountMapper;
}
