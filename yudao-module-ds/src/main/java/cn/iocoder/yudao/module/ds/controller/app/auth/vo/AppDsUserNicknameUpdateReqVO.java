package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - DS 用户昵称更新 Request VO")
@Data
public class AppDsUserNicknameUpdateReqVO {

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "三三用户")
    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 64, message = "昵称长度为 1-64 位")
    private String nickname;
}
