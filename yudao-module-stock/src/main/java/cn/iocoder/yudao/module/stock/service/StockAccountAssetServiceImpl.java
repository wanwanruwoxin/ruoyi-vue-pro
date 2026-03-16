package cn.iocoder.yudao.module.stock.service;

import cn.iocoder.yudao.module.stock.dal.mysql.StockAccountAssetMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class StockAccountAssetServiceImpl implements StockAccountAssetService {

    @Resource
    private StockAccountAssetMapper stockAccountAssetMapper;
}
