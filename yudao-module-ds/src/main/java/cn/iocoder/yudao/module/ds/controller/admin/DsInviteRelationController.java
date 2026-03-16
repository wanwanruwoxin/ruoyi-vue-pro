package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.module.ds.service.DsInviteRelationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 电商邀请关系")
@RestController
@RequestMapping("/ds/invite-relation")
@Validated
public class DsInviteRelationController {

    @Resource
    private DsInviteRelationService dsInviteRelationService;
}
