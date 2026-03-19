package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - DS 商品状态更新 Request VO")
@Data
public class DsProductUpdateStatusReqVO {

    @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "商品编号不能为空")
    private Long id;

    @Schema(description = "商品状态：-1 回收站，0 下架，1 上架", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "商品状态不能为空")
    private Integer saleStatus;
}
