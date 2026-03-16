package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.module.ds.service.DsMembershipPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 电商会员方案")
@RestController
@RequestMapping("/ds/membership-plan")
@Validated
public class DsMembershipPlanController {

    @Resource
    private DsMembershipPlanService dsMembershipPlanService;
}
