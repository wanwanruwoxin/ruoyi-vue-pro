package cn.iocoder.yudao.module.stock.controller.admin;

import cn.iocoder.yudao.module.stock.service.StockUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 股票用户")
@RestController
@RequestMapping("/stock/user")
@Validated
public class StockUserController {

    @Resource
    private StockUserService stockUserService;
}
