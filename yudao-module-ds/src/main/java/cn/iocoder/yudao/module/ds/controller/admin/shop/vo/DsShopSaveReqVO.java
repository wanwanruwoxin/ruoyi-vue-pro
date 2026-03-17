package cn.iocoder.yudao.module.ds.controller.admin.shop.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "管理后台 - DS 店铺新增/修改 Request VO")
@Data
public class DsShopSaveReqVO {

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "用户编号不能为空")
    private Long uid;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "星选商城官方店")
    @NotBlank(message = "店铺名称不能为空")
    @Length(min = 2, max = 64, message = "店铺名称长度为 2-64 位")
    private String shopName;

    @Schema(description = "店铺头像", example = "https://cdn.example.com/shop/avatar.png")
    private String avatarUrl;

    @Schema(description = "店铺简介", example = "专注优选好物")
    @Length(max = 500, message = "店铺简介长度不能超过 500 位")
    private String intro;

    @Schema(description = "联系方式手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "联系方式不能为空")
    @Mobile
    private String contactMobile;

    @Schema(description = "发货省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海市")
    @NotBlank(message = "发货省份不能为空")
    private String shipProvince;

    @Schema(description = "发货城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海市")
    @NotBlank(message = "发货城市不能为空")
    private String shipCity;

    @Schema(description = "发货区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "浦东新区")
    @NotBlank(message = "发货区县不能为空")
    private String shipDistrict;

    @Schema(description = "发货详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "世纪大道 100 号")
    @NotBlank(message = "发货详细地址不能为空")
    private String shipDetailAddress;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
