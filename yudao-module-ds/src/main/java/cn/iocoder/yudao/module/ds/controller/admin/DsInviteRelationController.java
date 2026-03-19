package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import cn.iocoder.yudao.module.ds.service.DsInviteRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商邀请关系")
@RestController
@RequestMapping("/ds/invite-relation")
@Validated
public class DsInviteRelationController {

    @Resource
    private DsInviteRelationService dsInviteRelationService;
    @Resource
    private DsInviteRelationMapper dsInviteRelationMapper;

    @GetMapping("/page")
    @Operation(summary = "邀请关系列表")
    public CommonResult<PageResult<DsInviteRelation>> getPage(PageParam pageParam,
                                                               @RequestParam(value = "inviterId", required = false) Long inviterId,
                                                               @RequestParam(value = "inviteeId", required = false) Long inviteeId,
                                                               @RequestParam(value = "level", required = false) Integer level,
                                                               @RequestParam(value = "bindStatus", required = false) Integer bindStatus) {
        return success(dsInviteRelationMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsInviteRelation>()
                .eqIfPresent(DsInviteRelation::getInviterId, inviterId)
                .eqIfPresent(DsInviteRelation::getInviteeId, inviteeId)
                .eqIfPresent(DsInviteRelation::getLevel, level)
                .eqIfPresent(DsInviteRelation::getBindStatus, bindStatus)
                .orderByDesc(DsInviteRelation::getId)));
    }
}
