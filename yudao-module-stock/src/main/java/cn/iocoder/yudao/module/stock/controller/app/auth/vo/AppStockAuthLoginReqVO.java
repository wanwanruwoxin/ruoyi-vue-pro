package cn.iocoder.yudao.module.stock.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "股票 APP - 登录 Request VO")
@Data
public class AppStockAuthLoginReqVO {

    @Schema(description = "账号（手机号或邮箱）", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}
