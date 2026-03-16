package cn.iocoder.yudao.module.stock.controller.admin;

import cn.iocoder.yudao.module.stock.service.StockAccountAssetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 股票账户资产")
@RestController
@RequestMapping("/stock/account-asset")
@Validated
public class StockAccountAssetController {

    @Resource
    private StockAccountAssetService stockAccountAssetService;
}
