package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsInviteRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Resource
    private DsUserMapper dsUserMapper;

    @GetMapping("/page")
    @Operation(summary = "邀请关系列表")
    public CommonResult<PageResult<DsInviteRelationAdminRespVO>> getPage(PageParam pageParam,
                                                                          @RequestParam(value = "inviterId", required = false) Long inviterId,
                                                                          @RequestParam(value = "inviteeId", required = false) Long inviteeId,
                                                                          @RequestParam(value = "level", required = false) Integer level,
                                                                          @RequestParam(value = "bindStatus", required = false) Integer bindStatus) {
        PageResult<DsInviteRelation> relationPage = dsInviteRelationMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsInviteRelation>()
                .eqIfPresent(DsInviteRelation::getInviterId, inviterId)
                .eqIfPresent(DsInviteRelation::getInviteeId, inviteeId)
                .eqIfPresent(DsInviteRelation::getLevel, level)
                .eqIfPresent(DsInviteRelation::getBindStatus, bindStatus)
                .orderByDesc(DsInviteRelation::getId));
        Set<Long> userIds = relationPage.getList().stream()
                .flatMap(item -> Stream.of(item.getInviterId(), item.getInviteeId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, String> userMobileMap = userIds.isEmpty() ? Map.of() : dsUserMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(DsUser::getId, DsUser::getMobile, (left, right) -> left));
        return success(new PageResult<>(relationPage.getList().stream().map(item -> {
            DsInviteRelationAdminRespVO vo = new DsInviteRelationAdminRespVO();
            vo.setId(item.getId());
            vo.setInviterId(item.getInviterId());
            vo.setInviteeId(item.getInviteeId());
            vo.setInviterMobile(userMobileMap.get(item.getInviterId()));
            vo.setInviteeMobile(userMobileMap.get(item.getInviteeId()));
            vo.setLevel(item.getLevel());
            vo.setSourceQrCode(item.getSourceQrCode());
            vo.setBindStatus(item.getBindStatus());
            vo.setCreateTime(item.getCreateTime());
            return vo;
        }).toList(), relationPage.getTotal()));
    }

    @Data
    public static class DsInviteRelationAdminRespVO {

        private Long id;
        private Long inviterId;
        private Long inviteeId;
        private String inviterMobile;
        private String inviteeMobile;
        private Integer level;
        private String sourceQrCode;
        private Integer bindStatus;
        private java.time.LocalDateTime createTime;
    }
}
