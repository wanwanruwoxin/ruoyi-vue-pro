package cn.iocoder.yudao.module.ds.controller.app.point.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - DS 积分赠送 Request VO")
@Data
public class AppDsPointGiftReqVO {

    @Schema(description = "赠送对象手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "赠送对象手机号不能为空")
    @Mobile
    private String targetMobile;

    @Schema(description = "赠送积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "赠送积分不能为空")
    @DecimalMin(value = "0.01", message = "赠送积分必须大于 0")
    private BigDecimal points;
}
