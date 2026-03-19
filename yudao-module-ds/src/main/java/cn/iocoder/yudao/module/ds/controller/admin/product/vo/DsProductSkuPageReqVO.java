package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - DS 商品 SKU 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsProductSkuPageReqVO extends PageParam {

    @Schema(description = "SPU 编号", example = "1")
    private Long spuId;

    @Schema(description = "SKU 名称", example = "红色")
    private String name;

    @Schema(description = "商品条码", example = "123456")
    private String barCode;
}
