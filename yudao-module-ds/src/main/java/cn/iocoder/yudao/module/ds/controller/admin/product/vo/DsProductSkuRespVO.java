package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商品 SKU Response VO")
@Data
public class DsProductSkuRespVO {

    @Schema(description = "SKU 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "SPU 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long spuId;

    @Schema(description = "SKU 名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "红色")
    private String name;

    @Schema(description = "属性 JSON", example = "[{\"propertyId\":1,\"valueId\":1}]")
    private String propertiesJson;

    @Schema(description = "售价", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    private BigDecimal priceAmount;

    @Schema(description = "市场价", example = "129.00")
    private BigDecimal marketPrice;

    @Schema(description = "成本价", example = "59.00")
    private BigDecimal costPrice;

    @Schema(description = "商品条码", example = "123456")
    private String barCode;

    @Schema(description = "图片地址", example = "https://a.png")
    private String picUrl;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Integer stock;

    @Schema(description = "重量", example = "1.20")
    private Double weight;

    @Schema(description = "体积", example = "0.02")
    private Double volume;

    @Schema(description = "一级分销佣金", example = "100")
    private Integer firstBrokeragePrice;

    @Schema(description = "二级分销佣金", example = "50")
    private Integer secondBrokeragePrice;

    @Schema(description = "销量", example = "10")
    private Integer salesCount;

    @Schema(description = "上架时间")
    private LocalDateTime saleTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
