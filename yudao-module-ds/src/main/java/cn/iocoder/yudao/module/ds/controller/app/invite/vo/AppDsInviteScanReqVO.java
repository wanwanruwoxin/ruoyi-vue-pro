package cn.iocoder.yudao.module.ds.controller.app.invite.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - DS 邀请扫码 Request VO")
@Data
public class AppDsInviteScanReqVO {

    @Schema(description = "邀请人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "邀请人用户编号不能为空")
    private Long inviterId;

    @Schema(description = "被邀请人手机号", example = "15601691300")
    @Mobile
    private String mobile;
}
