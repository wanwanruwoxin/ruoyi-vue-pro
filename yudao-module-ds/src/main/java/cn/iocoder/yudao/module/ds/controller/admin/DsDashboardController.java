package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipAccountMapper;
import cn.iocoder.yudao.module.ds.service.DsMerchantScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.ADVANCED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.NORMAL;

@Tag(name = "管理后台 - DS首页")
@RestController
@RequestMapping("/ds/dashboard")
@Validated
public class DsDashboardController {

    @Resource
    private DsInviteRelationMapper dsInviteRelationMapper;
    @Resource
    private DsMembershipAccountMapper dsMembershipAccountMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/team-overview")
    @Operation(summary = "当前登录用户团队概览")
    public CommonResult<TeamOverviewRespVO> getTeamOverview() {
        try {
            Long dsUid = dsMerchantScopeService.getCurrentDsUid();
            List<Long> level1InviteeIds = extractInviteeIds(dsInviteRelationMapper.selectListByInviterIdAndLevel(dsUid, 1));
            List<Long> level2InviteeIds = extractInviteeIds(dsInviteRelationMapper.selectListByInviterIdAndLevel(dsUid, 2));
            List<Long> level3PlusInviteeIds = extractInviteeIds(dsInviteRelationMapper.selectListByInviterIdAndMinLevel(dsUid, 3));
            TeamOverviewRespVO respVO = buildDefaultOverview();
            respVO.setLevel1UserCount(level1InviteeIds.size());
            respVO.setLevel2UserCount(level2InviteeIds.size());
            respVO.setLevel3PlusUserCount(level3PlusInviteeIds.size());
            respVO.setLevel1NormalMemberCount(countPlanMembers(level1InviteeIds, NORMAL.getCode()));
            respVO.setLevel1AdvancedMemberCount(countPlanMembers(level1InviteeIds, ADVANCED.getCode()));
            respVO.setLevel2NormalMemberCount(countPlanMembers(level2InviteeIds, NORMAL.getCode()));
            respVO.setLevel2AdvancedMemberCount(countPlanMembers(level2InviteeIds, ADVANCED.getCode()));
            respVO.setLevel3PlusNormalMemberCount(countPlanMembers(level3PlusInviteeIds, NORMAL.getCode()));
            respVO.setLevel3PlusAdvancedMemberCount(countPlanMembers(level3PlusInviteeIds, ADVANCED.getCode()));
            return success(respVO);
        } catch (ServiceException ex) {
            if (SHOP_NOT_EXISTS.getCode().equals(ex.getCode())) {
                return success(buildDefaultOverview());
            }
            throw ex;
        }
    }

    private List<Long> extractInviteeIds(List<DsInviteRelation> relations) {
        return relations.stream().map(DsInviteRelation::getInviteeId).toList();
    }

    private int countPlanMembers(List<Long> inviteeIds, String planCode) {
        return dsMembershipAccountMapper.selectCountByUidsAndPlanCode(inviteeIds, planCode);
    }

    private TeamOverviewRespVO buildDefaultOverview() {
        TeamOverviewRespVO respVO = new TeamOverviewRespVO();
        respVO.setLevel1UserCount(0);
        respVO.setLevel2UserCount(0);
        respVO.setLevel3PlusUserCount(0);
        respVO.setLevel1NormalMemberCount(0);
        respVO.setLevel1AdvancedMemberCount(0);
        respVO.setLevel2NormalMemberCount(0);
        respVO.setLevel2AdvancedMemberCount(0);
        respVO.setLevel3PlusNormalMemberCount(0);
        respVO.setLevel3PlusAdvancedMemberCount(0);
        return respVO;
    }

    @Data
    public static class TeamOverviewRespVO {

        private Integer level1UserCount;
        private Integer level2UserCount;
        private Integer level3PlusUserCount;
        private Integer level1NormalMemberCount;
        private Integer level1AdvancedMemberCount;
        private Integer level2NormalMemberCount;
        private Integer level2AdvancedMemberCount;
        private Integer level3PlusNormalMemberCount;
        private Integer level3PlusAdvancedMemberCount;
    }
}
