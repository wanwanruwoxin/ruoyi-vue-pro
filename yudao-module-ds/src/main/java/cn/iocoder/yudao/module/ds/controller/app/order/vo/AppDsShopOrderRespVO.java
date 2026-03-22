package cn.iocoder.yudao.module.ds.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - DS 商城订单 Response VO")
@Data
public class AppDsShopOrderRespVO {

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "S67f0b13ea8df4dc5b5da5f888f25c395")
    private String orderNo;

    @Schema(description = "商品总件数", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Integer itemCount;

    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "88")
    private BigDecimal totalAmount;

    @Schema(description = "支付积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "88")
    private BigDecimal payPoints;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAID")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;

    @Schema(description = "商品摘要", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productSummary;
}
