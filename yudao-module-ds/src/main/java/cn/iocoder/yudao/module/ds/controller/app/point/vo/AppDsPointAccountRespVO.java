package cn.iocoder.yudao.module.ds.controller.app.point.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "用户 APP - DS 积分账户 Response VO")
@Data
public class AppDsPointAccountRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long uid;

    @Schema(description = "可用积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "120.50")
    private BigDecimal availablePoints;

    @Schema(description = "冻结积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "20.00")
    private BigDecimal frozenPoints;

    @Schema(description = "累计获得积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.00")
    private BigDecimal totalEarnedPoints;

    @Schema(description = "累计支出积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "379.50")
    private BigDecimal totalSpentPoints;
}
