package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipAccountMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointAccountMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商用户")
@RestController
@RequestMapping("/ds/user")
@Validated
public class DsUserController {

    @Resource
    private DsUserService dsUserService;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsMembershipAccountMapper dsMembershipAccountMapper;
    @Resource
    private DsPointAccountMapper dsPointAccountMapper;

    @GetMapping("/page")
    @Operation(summary = "用户分页")
    public CommonResult<PageResult<DsUserAdminRespVO>> getPage(PageParam pageParam,
                                                               @RequestParam(value = "mobile", required = false) String mobile,
                                                               @RequestParam(value = "nickname", required = false) String nickname,
                                                               @RequestParam(value = "status", required = false) Integer status) {
        PageResult<DsUser> userPage = dsUserMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsUser>()
                .likeIfPresent(DsUser::getMobile, mobile)
                .likeIfPresent(DsUser::getNickname, nickname)
                .eqIfPresent(DsUser::getStatus, status)
                .orderByDesc(DsUser::getId));
        List<DsUserAdminRespVO> result = userPage.getList().stream().map(user -> {
            DsUserAdminRespVO vo = new DsUserAdminRespVO();
            BeanUtils.copyProperties(user, vo);
            DsMembershipAccount account = dsMembershipAccountMapper.selectByUid(user.getId());
            if (account != null) {
                vo.setCurrentPlanCode(account.getCurrentPlanCode());
                vo.setMemberStatus(account.getMemberStatus());
                vo.setTeamLeader(account.getTeamLeader());
                vo.setShareholder(account.getShareholder());
            }
            DsPointAccount pointAccount = dsPointAccountMapper.selectByUid(user.getId());
            vo.setAvailablePoints(pointAccount == null ? BigDecimal.ZERO : pointAccount.getAvailablePoints());
            return vo;
        }).toList();
        return success(new PageResult<>(result, userPage.getTotal()));
    }

    @Data
    public static class DsUserAdminRespVO {

        private Long id;
        private String mobile;
        private String nickname;
        private String avatar;
        private String registerChannel;
        private Integer status;
        private String currentPlanCode;
        private String memberStatus;
        private Integer teamLeader;
        private Integer shareholder;
        private BigDecimal availablePoints;
    }
}
