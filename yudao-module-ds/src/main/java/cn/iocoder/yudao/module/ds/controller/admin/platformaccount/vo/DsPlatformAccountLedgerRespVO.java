package cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 平台账户流水 Response VO")
@Data
public class DsPlatformAccountLedgerRespVO {

    @Schema(description = "流水编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "账户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long accountId;

    @Schema(description = "账户编码", example = "MAIN")
    private String accountCode;

    @Schema(description = "变更类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "INCOME")
    private String changeType;

    @Schema(description = "变更金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "变更后余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balanceAfter;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "SHOP_ORDER_COMMISSION")
    private String bizType;

    @Schema(description = "业务单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bizNo;

    @Schema(description = "订单编号")
    private Long orderId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单项编号")
    private Long orderItemId;

    @Schema(description = "分账编号")
    private Long splitId;

    @Schema(description = "发生时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime occurredAt;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
