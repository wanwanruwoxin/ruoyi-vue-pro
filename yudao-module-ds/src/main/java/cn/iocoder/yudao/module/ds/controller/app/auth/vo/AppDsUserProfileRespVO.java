package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - DS 用户资料 Response VO")
@Data
public class AppDsUserProfileRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long userId;

    @Schema(description = "用户昵称", example = "三三用户")
    private String nickname;

    @Schema(description = "用户头像", example = "https://www.iocoder.cn/avatar.png")
    private String avatar;
}
