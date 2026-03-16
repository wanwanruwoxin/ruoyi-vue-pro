package cn.iocoder.yudao.module.ds.controller.app.invite.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户 APP - DS 邀请扫码 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDsInviteScanRespVO {

    @Schema(description = "邀请人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long inviterId;

    @Schema(description = "邀请链接", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inviteLink;

    @Schema(description = "是否已存在账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean accountExists;

    @Schema(description = "提示信息", requiredMode = Schema.RequiredMode.REQUIRED, example = "已存在账号")
    private String message;

    @Schema(description = "绑定策略", requiredMode = Schema.RequiredMode.REQUIRED, example = "LOGIN_BIND_REQUIRED")
    private String bindStrategy;
}
