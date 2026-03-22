package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - DS 用户密码更新 Request VO")
@Data
public class AppDsUserPasswordUpdateReqVO {

    @Schema(description = "旧密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "旧密码不能为空")
    @Length(min = 6, max = 32, message = "旧密码长度为 6-32 位")
    private String oldPassword;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "654321")
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 32, message = "新密码长度为 6-32 位")
    private String newPassword;
}
