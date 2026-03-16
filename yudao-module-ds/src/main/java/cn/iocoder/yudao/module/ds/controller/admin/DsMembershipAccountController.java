package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.module.ds.service.DsMembershipAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 电商会员账户")
@RestController
@RequestMapping("/ds/membership-account")
@Validated
public class DsMembershipAccountController {

    @Resource
    private DsMembershipAccountService dsMembershipAccountService;
}
