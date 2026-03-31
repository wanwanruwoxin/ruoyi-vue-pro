package cn.iocoder.yudao.module.ds.controller.app.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "用户 APP - DS 商品 Response VO")
@Data
public class AppDsProductRespVO {

    @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "轻食代餐组合")
    private String productName;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    private BigDecimal priceAmount;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer stock;

    @Schema(description = "详情描述")
    private String detailDesc;

    @Schema(description = "商品图片地址列表")
    private List<String> imageUrls;

    @Schema(description = "商品首图")
    private String coverImage;

    @Schema(description = "商品轮播图")
    private List<String> carouselImages;

    @Schema(description = "商品视频地址列表")
    private List<String> videoUrls;

    @Schema(description = "销售状态：0 下架，1 上架", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer saleStatus;

    @Schema(description = "排序值，越小越靠前", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer sort;
}
