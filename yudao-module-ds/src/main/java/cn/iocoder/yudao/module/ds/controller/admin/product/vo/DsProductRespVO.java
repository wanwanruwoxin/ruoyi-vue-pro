package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商品 Response VO")
@Data
public class DsProductRespVO {

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

    @Schema(description = "详情描述", example = "甄选 8 种低脂食材")
    private String detailDesc;

    @Schema(description = "商品图片，多个用英文逗号分隔", example = "https://a.png,https://b.png")
    private String imageUrls;

    @Schema(description = "商品视频，多个用英文逗号分隔", example = "https://a.mp4,https://b.mp4")
    private String videoUrls;

    @Schema(description = "销售状态：0 下架，1 上架", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer saleStatus;

    @Schema(description = "排序值，越小越靠前", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer sort;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
