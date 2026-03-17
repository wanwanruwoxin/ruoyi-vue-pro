package cn.iocoder.yudao.module.ds.controller.app.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "用户 APP - DS 我的商品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppDsMyProductPageReqVO extends PageParam {

    @Schema(description = "商品关键字", example = "代餐")
    private String keyword;

    @Schema(description = "销售状态：0 下架，1 上架", example = "1")
    private Integer saleStatus;

    @Schema(description = "排序方式：DEFAULT/PRICE_ASC/PRICE_DESC/STOCK_ASC/STOCK_DESC/LATEST", example = "DEFAULT")
    private String sortType;
}
