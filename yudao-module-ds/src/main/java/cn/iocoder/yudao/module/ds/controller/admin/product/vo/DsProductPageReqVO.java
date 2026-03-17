package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - DS 商品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsProductPageReqVO extends PageParam {

    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "商品名称关键字", example = "代餐")
    private String productName;

    @Schema(description = "销售状态：0 下架，1 上架", example = "1")
    private Integer saleStatus;
}
