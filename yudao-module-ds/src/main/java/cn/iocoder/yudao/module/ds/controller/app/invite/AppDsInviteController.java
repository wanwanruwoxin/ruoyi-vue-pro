package cn.iocoder.yudao.module.ds.controller.app.invite;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteBindReqVO;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteLinkRespVO;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteScanReqVO;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteScanRespVO;
import cn.iocoder.yudao.module.ds.service.DsInviteRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 邀请")
@RestController
@RequestMapping("/ds/invite")
@Validated
public class AppDsInviteController {

    @Resource
    private DsInviteRelationService dsInviteRelationService;

    @PostMapping("/link")
    @Operation(summary = "获取我的邀请链接和二维码内容")
    public CommonResult<AppDsInviteLinkRespVO> getInviteLink() {
        return success(dsInviteRelationService.getInviteLink(getLoginUserId()));
    }

    @PostMapping("/scan")
    @Operation(summary = "扫码校验邀请信息")
    @PermitAll
    public CommonResult<AppDsInviteScanRespVO> scanInvite(@RequestBody @Valid AppDsInviteScanReqVO reqVO) {
        return success(dsInviteRelationService.scanInvite(reqVO.getInviterId(), reqVO.getMobile()));
    }

    @PostMapping("/bind")
    @Operation(summary = "已注册用户绑定邀请关系")
    public CommonResult<Boolean> bindInvite(@RequestBody @Valid AppDsInviteBindReqVO reqVO) {
        dsInviteRelationService.bindInviteRelation(reqVO.getInviterId(), getLoginUserId());
        return success(true);
    }
}
