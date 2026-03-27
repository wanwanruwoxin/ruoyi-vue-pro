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

    @Schema(description = "订单项编号", example = "1")
    private Long orderItemId;

    @Schema(description = "支付积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "88")
    private BigDecimal payPoints;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAID")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;

    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "店铺名称", example = "三三生活自营店")
    private String shopName;

    @Schema(description = "商品编号", example = "1001")
    private Long productId;

    @Schema(description = "商品名称", example = "DS测试商品")
    private String productName;

    @Schema(description = "商品图片", example = "https://example.com/a.png")
    private String productImage;

    @Schema(description = "商品规格", example = "默认规格")
    private String productSpec;

    @Schema(description = "购买数量", example = "2")
    private Integer quantity;
}
