package cn.iocoder.yudao.module.ds.controller.admin.order.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商城订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsShopOrderPageReqVO extends PageParam {

    @Schema(description = "订单编号", example = "S202601010001")
    private String orderNo;

    @Schema(description = "用户编号", example = "100")
    private Long uid;

    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "支付状态", example = "PAID")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime[] paidAt;
}
