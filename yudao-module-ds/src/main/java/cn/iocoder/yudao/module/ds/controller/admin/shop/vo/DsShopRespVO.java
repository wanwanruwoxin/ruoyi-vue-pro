package cn.iocoder.yudao.module.ds.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 店铺 Response VO")
@Data
public class DsShopRespVO {

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long uid;

    @Schema(description = "用户名称", example = "张三")
    private String userNickname;

    @Schema(description = "用户手机号", example = "15601691300")
    private String userMobile;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "星选商城官方店")
    private String shopName;

    @Schema(description = "店铺头像", example = "https://cdn.example.com/shop/avatar.png")
    private String avatarUrl;

    @Schema(description = "店铺简介", example = "专注优选好物")
    private String intro;

    @Schema(description = "联系方式手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    private String contactMobile;

    @Schema(description = "发货省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海市")
    private String shipProvince;

    @Schema(description = "发货城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海市")
    private String shipCity;

    @Schema(description = "发货区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "浦东新区")
    private String shipDistrict;

    @Schema(description = "发货详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "世纪大道 100 号")
    private String shipDetailAddress;

    @Schema(description = "店铺状态（0待审核 1审核通过 2审核拒绝）", example = "0")
    private Integer status;

    @Schema(description = "审核备注")
    private String auditRemark;

    @Schema(description = "审核管理员编号")
    private Long auditAdminUserId;

    @Schema(description = "商家后台账号编号")
    private Long backendAdminUserId;

    @Schema(description = "商家后台账号用户名")
    private String backendAdminUsername;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
