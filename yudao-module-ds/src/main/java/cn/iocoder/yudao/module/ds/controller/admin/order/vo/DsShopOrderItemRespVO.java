package cn.iocoder.yudao.module.ds.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商城订单项 Response VO")
@Data
public class DsShopOrderItemRespVO {

    @Schema(description = "订单项编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long orderId;

    @Schema(description = "订单单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "S202601010001")
    private String orderNo;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long uid;

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    private Long productId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "轻食沙拉")
    private String productName;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "39.90")
    private BigDecimal priceAmount;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer quantity;

    @Schema(description = "行金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "79.80")
    private BigDecimal lineAmount;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;
}
