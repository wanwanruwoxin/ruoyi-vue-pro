package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - DS 商品 Response VO")
@Data
public class DsProductRespVO {

    @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long shopId;

    @Schema(description = "分类编号", example = "12")
    private Long categoryId;

    @Schema(description = "品牌编号", example = "1")
    private Long brandId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "轻食代餐组合")
    private String productName;

    @Schema(description = "商品关键词", example = "轻食,低脂")
    private String keyword;

    @Schema(description = "商品简介", example = "轻食套餐")
    private String introduction;

    @Schema(description = "商品详情", example = "<p>商品详情</p>")
    private String description;

    @Schema(description = "商品封面图", example = "https://a.png")
    private String picUrl;

    @Schema(description = "商品轮播图，多个用英文逗号分隔", example = "https://a.png,https://b.png")
    private String sliderPicUrls;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    private BigDecimal priceAmount;

    @Schema(description = "市场价", example = "199.00")
    private BigDecimal marketPrice;

    @Schema(description = "成本价", example = "59.00")
    private BigDecimal costPrice;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer stock;

    @Schema(description = "详情描述", example = "甄选 8 种低脂食材")
    private String detailDesc;

    @Schema(description = "商品图片，多个用英文逗号分隔", example = "https://a.png,https://b.png")
    private String imageUrls;

    @Schema(description = "商品视频，多个用英文逗号分隔", example = "https://a.mp4,https://b.mp4")
    private String videoUrls;

    @Schema(description = "规格类型：false 单规格，true 多规格", example = "false")
    private Boolean specType;

    @Schema(description = "配送方式数组，逗号分隔", example = "1,2")
    private String deliveryTypes;

    @Schema(description = "物流模板编号", example = "1")
    private Long deliveryTemplateId;

    @Schema(description = "赠送积分", example = "100")
    private Integer giveIntegral;

    @Schema(description = "分销类型：false 默认，true 自定义", example = "false")
    private Boolean subCommissionType;

    @Schema(description = "销售状态：0 下架，1 上架", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer saleStatus;

    @Schema(description = "排序值，越小越靠前", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer sort;

    @Schema(description = "销量", example = "10")
    private Integer salesCount;

    @Schema(description = "虚拟销量", example = "10")
    private Integer virtualSalesCount;

    @Schema(description = "浏览量", example = "100")
    private Integer browseCount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "SKU 列表")
    private List<DsProductSkuRespVO> skus;
}
