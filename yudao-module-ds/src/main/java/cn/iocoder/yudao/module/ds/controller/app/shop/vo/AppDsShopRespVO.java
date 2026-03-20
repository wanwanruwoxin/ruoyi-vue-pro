package cn.iocoder.yudao.module.ds.controller.app.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - DS 店铺 Response VO")
@Data
public class AppDsShopRespVO {

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "店铺所属用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long uid;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "星选商城官方店")
    private String shopName;

    @Schema(description = "店铺头像", example = "https://cdn.example.com/shop/avatar.png")
    private String avatarUrl;

    @Schema(description = "店铺简介")
    private String intro;

    @Schema(description = "联系方式手机号", example = "15601691300")
    private String contactMobile;

    @Schema(description = "发货省份")
    private String shipProvince;

    @Schema(description = "发货城市")
    private String shipCity;

    @Schema(description = "发货区县")
    private String shipDistrict;

    @Schema(description = "发货详细地址")
    private String shipDetailAddress;

    @Schema(description = "店铺状态（0待审核 1审核通过 2审核拒绝）", example = "0")
    private Integer status;

    @Schema(description = "审核备注")
    private String auditRemark;
}
