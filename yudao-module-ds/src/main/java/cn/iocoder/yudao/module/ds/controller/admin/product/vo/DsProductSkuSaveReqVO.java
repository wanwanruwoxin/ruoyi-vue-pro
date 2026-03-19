package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - DS 商品 SKU 新增/修改 Request VO")
@Data
public class DsProductSkuSaveReqVO {

    @Schema(description = "SKU 编号", example = "1")
    private Long id;

    @Schema(description = "SPU 编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "SPU 编号不能为空")
    private Long spuId;

    @Schema(description = "SKU 名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "红色")
    @NotBlank(message = "SKU 名称不能为空")
    @Length(max = 128, message = "SKU 名称长度不能超过 128 位")
    private String name;

    @Schema(description = "属性 JSON", example = "[{\"propertyId\":1,\"valueId\":1}]")
    private String propertiesJson;

    @Schema(description = "售价", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    @NotNull(message = "售价不能为空")
    @DecimalMin(value = "0.01", message = "售价必须大于 0")
    private BigDecimal priceAmount;

    @Schema(description = "市场价", example = "129.00")
    @DecimalMin(value = "0", message = "市场价不能小于 0")
    private BigDecimal marketPrice;

    @Schema(description = "成本价", example = "59.00")
    @DecimalMin(value = "0", message = "成本价不能小于 0")
    private BigDecimal costPrice;

    @Schema(description = "商品条码", example = "123456")
    @Length(max = 128, message = "商品条码长度不能超过 128 位")
    private String barCode;

    @Schema(description = "图片地址", example = "https://a.png")
    @Length(max = 512, message = "图片地址长度不能超过 512 位")
    private String picUrl;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于 0")
    private Integer stock;

    @Schema(description = "重量", example = "1.20")
    private Double weight;

    @Schema(description = "体积", example = "0.02")
    private Double volume;

    @Schema(description = "一级分销佣金", example = "100")
    @Min(value = 0, message = "一级分销佣金不能小于 0")
    private Integer firstBrokeragePrice;

    @Schema(description = "二级分销佣金", example = "50")
    @Min(value = 0, message = "二级分销佣金不能小于 0")
    private Integer secondBrokeragePrice;

    @Schema(description = "销量", example = "10")
    @Min(value = 0, message = "销量不能小于 0")
    private Integer salesCount;

    @Schema(description = "上架时间")
    private LocalDateTime saleTime;
}
