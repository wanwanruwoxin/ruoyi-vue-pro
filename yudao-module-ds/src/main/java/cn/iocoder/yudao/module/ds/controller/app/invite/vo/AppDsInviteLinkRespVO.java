package cn.iocoder.yudao.module.ds.controller.app.invite.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "用户 APP - DS 邀请链接 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppDsInviteLinkRespVO {

    @Schema(description = "邀请人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long inviterId;

    @Schema(description = "邀请链接", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inviteLink;

    @Schema(description = "邀请二维码内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String inviteQrCodeContent;
}
