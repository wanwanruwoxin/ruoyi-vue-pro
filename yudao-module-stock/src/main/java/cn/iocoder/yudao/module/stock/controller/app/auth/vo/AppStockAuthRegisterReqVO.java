package cn.iocoder.yudao.module.stock.controller.app.auth.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "股票 APP - 注册 Request VO")
@Data
public class AppStockAuthRegisterReqVO {

    @Schema(description = "手机号", example = "15601691300")
    private String phone;

    @Schema(description = "邮箱", example = "stock@iocoder.cn")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 32, message = "密码长度为 6-32 位")
    private String password;

    @AssertTrue(message = "手机号和邮箱不能同时为空")
    public boolean isPhoneOrEmailPresent() {
        return StrUtil.isNotBlank(phone) || StrUtil.isNotBlank(email);
    }
}
