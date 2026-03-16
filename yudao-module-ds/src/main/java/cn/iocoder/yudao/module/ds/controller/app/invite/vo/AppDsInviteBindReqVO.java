package cn.iocoder.yudao.module.ds.controller.app.invite.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - DS 邀请绑定 Request VO")
@Data
public class AppDsInviteBindReqVO {

    @Schema(description = "邀请人用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    @NotNull(message = "邀请人用户编号不能为空")
    private Long inviterId;
}
