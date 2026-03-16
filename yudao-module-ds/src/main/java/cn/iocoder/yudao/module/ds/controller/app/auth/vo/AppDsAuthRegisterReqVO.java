package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - DS 注册 Request VO")
@Data
public class AppDsAuthRegisterReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "小芋")
    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 64, message = "昵称长度为 1-64 位")
    private String nickname;

    @Schema(description = "头像", example = "https://www.iocoder.cn/avatar.png")
    private String avatar;

    @Schema(description = "注册渠道", requiredMode = Schema.RequiredMode.REQUIRED, example = "APP")
    @NotBlank(message = "注册渠道不能为空")
    private String registerChannel;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 32, message = "密码长度为 6-32 位")
    private String password;
}
