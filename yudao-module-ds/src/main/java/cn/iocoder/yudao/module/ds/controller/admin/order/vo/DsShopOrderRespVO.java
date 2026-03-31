package cn.iocoder.yudao.module.ds.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商城订单 Response VO")
@Data
public class DsShopOrderRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "订单单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "S202601010001")
    private String orderNo;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long uid;

    @Schema(description = "用户昵称", example = "张三")
    private String userNickname;

    @Schema(description = "用户手机号", example = "15601691300")
    private String userMobile;

    @Schema(description = "关联店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "关联店铺名称", example = "官方店")
    private String shopName;

    @Schema(description = "商品件数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer itemCount;

    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    private BigDecimal totalAmount;

    @Schema(description = "支付积分", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    private BigDecimal payPoints;

    @Schema(description = "支付状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "PAID")
    private String payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;

    @Schema(description = "商品摘要", example = "苹果 x1；香蕉 x2")
    private String productSummary;

    @Schema(description = "收货人姓名", example = "张三")
    private String receiverName;

    @Schema(description = "收货人手机号", example = "15601691300")
    private String receiverMobile;

    @Schema(description = "收货省", example = "北京市")
    private String receiverProvince;

    @Schema(description = "收货市", example = "北京市")
    private String receiverCity;

    @Schema(description = "收货区", example = "朝阳区")
    private String receiverDistrict;

    @Schema(description = "收货详细地址", example = "望京街道 88 号")
    private String receiverDetailAddress;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
