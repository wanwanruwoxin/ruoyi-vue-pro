package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - DS 会员订单 Response VO")
@Data
public class AppDsMembershipOrderRespVO {

    @Schema(description = "会员订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "M67f0b13ea8df4dc5b5da5f888f25c395")
    private String orderNo;

    @Schema(description = "会员方案编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long planId;

    @Schema(description = "应付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "199")
    private BigDecimal payableAmount;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "PENDING")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;

    @Schema(description = "退款状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "NONE")
    private String refundStatus;
}
