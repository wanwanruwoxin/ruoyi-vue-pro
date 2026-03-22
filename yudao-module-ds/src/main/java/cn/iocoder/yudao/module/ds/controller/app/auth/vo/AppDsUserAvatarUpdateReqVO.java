package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "用户 APP - DS 用户头像更新 Request VO")
@Data
public class AppDsUserAvatarUpdateReqVO {

    @Schema(description = "用户头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/avatar.png")
    @NotBlank(message = "头像不能为空")
    private String avatar;
}
