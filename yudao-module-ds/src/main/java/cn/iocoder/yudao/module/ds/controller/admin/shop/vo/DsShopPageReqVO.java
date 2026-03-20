package cn.iocoder.yudao.module.ds.controller.admin.shop.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - DS 店铺分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsShopPageReqVO extends PageParam {

    @Schema(description = "用户编号", example = "100")
    private Long uid;

    @Schema(description = "店铺名称", example = "官方店")
    private String shopName;

    @Schema(description = "联系方式", example = "15601691300")
    private String contactMobile;

    @Schema(description = "店铺状态（0待审核 1审核通过 2审核拒绝）", example = "0")
    private Integer status;
}
