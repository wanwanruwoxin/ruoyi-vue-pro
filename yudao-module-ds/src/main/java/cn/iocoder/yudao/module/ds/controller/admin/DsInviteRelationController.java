package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
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
    private DsInviteRelationMapper dsInviteRelationMapper;
    @Resource
    private DsUserMapper dsUserMapper;

    @GetMapping("/page")
    @Operation(summary = "邀请关系列表")
    public CommonResult<PageResult<DsInviteRelationAdminRespVO>> getPage(PageParam pageParam,
                                                                          @RequestParam(value = "inviterId", required = false) Long inviterId,
                                                                          @RequestParam(value = "inviteeId", required = false) Long inviteeId,
                                                                          @RequestParam(value = "level", required = false) Integer level,
                                                                          @RequestParam(value = "levelMin", required = false) Integer levelMin,
                                                                          @RequestParam(value = "levelMax", required = false) Integer levelMax,
                                                                          @RequestParam(value = "bindStatus", required = false) Integer bindStatus) {
        PageResult<DsInviteRelation> relationPage = dsInviteRelationMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsInviteRelation>()
                .eqIfPresent(DsInviteRelation::getInviterId, inviterId)
                .eqIfPresent(DsInviteRelation::getInviteeId, inviteeId)
                .eqIfPresent(DsInviteRelation::getLevel, level)
                .geIfPresent(DsInviteRelation::getLevel, levelMin)
                .leIfPresent(DsInviteRelation::getLevel, levelMax)
                .eqIfPresent(DsInviteRelation::getBindStatus, bindStatus)
                .orderByDesc(DsInviteRelation::getId));
        Set<Long> userIds = relationPage.getList().stream()
                .flatMap(item -> Stream.of(item.getInviterId(), item.getInviteeId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Map.of() : dsUserMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(DsUser::getId, user -> user, (left, right) -> left));
        return success(new PageResult<>(relationPage.getList().stream().map(item -> {
            DsInviteRelationAdminRespVO vo = new DsInviteRelationAdminRespVO();
            vo.setId(item.getId());
            vo.setInviterId(item.getInviterId());
            vo.setInviteeId(item.getInviteeId());
            DsUser inviter = userMap.get(item.getInviterId());
            vo.setInviterMobile(inviter == null ? null : inviter.getMobile());
            vo.setInviterNickname(inviter == null ? null : inviter.getNickname());
            DsUser invitee = userMap.get(item.getInviteeId());
            vo.setInviteeMobile(invitee == null ? null : invitee.getMobile());
            vo.setInviteeNickname(invitee == null ? null : invitee.getNickname());
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
        private String inviterNickname;
        private String inviteeMobile;
        private String inviteeNickname;
        private Integer level;
        private String sourceQrCode;
        private Integer bindStatus;
        private java.time.LocalDateTime createTime;
    }
}
