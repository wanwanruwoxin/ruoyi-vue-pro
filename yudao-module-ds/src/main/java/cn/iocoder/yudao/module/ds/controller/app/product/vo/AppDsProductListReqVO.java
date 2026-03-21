package cn.iocoder.yudao.module.ds.controller.app.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "用户 APP - DS 店铺商品列表 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDsProductListReqVO extends PageParam {

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "商品关键字", example = "代餐")
    private String keyword;

    @Schema(description = "商品分类编号", example = "1001")
    private Long categoryId;

    @Schema(description = "排序方式：DEFAULT/PRICE_ASC/PRICE_DESC/STOCK_ASC/STOCK_DESC/LATEST", example = "DEFAULT")
    private String sortType;
}
