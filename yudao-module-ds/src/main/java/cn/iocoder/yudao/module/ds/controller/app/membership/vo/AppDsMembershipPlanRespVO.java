package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - DS 会员档位 Response VO")
@Data
public class AppDsMembershipPlanRespVO {

    @Schema(description = "方案编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "方案编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "NORMAL")
    private String planCode;

    @Schema(description = "方案名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "普通会员")
    private String planName;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "199")
    private BigDecimal priceAmount;

    @Schema(description = "有效天数", requiredMode = Schema.RequiredMode.REQUIRED, example = "365")
    private Integer durationDays;
}
