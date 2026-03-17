package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Schema(description = "管理后台 - DS 商品新增/修改 Request VO")
@Data
public class DsProductSaveReqVO {

    @Schema(description = "商品编号", example = "1")
    private Long id;

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long shopId;

    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "轻食代餐组合")
    @NotBlank(message = "商品名称不能为空")
    @Length(max = 64, message = "商品名称长度不能超过 64 位")
    private String productName;

    @Schema(description = "商品价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.00")
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于 0")
    private BigDecimal priceAmount;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能小于 0")
    private Integer stock;

    @Schema(description = "详情描述", example = "甄选 8 种低脂食材")
    @Length(max = 2000, message = "详情描述长度不能超过 2000 位")
    private String detailDesc;

    @Schema(description = "商品图片，多个用英文逗号分隔", example = "https://a.png,https://b.png")
    private String imageUrls;

    @Schema(description = "商品视频，多个用英文逗号分隔", example = "https://a.mp4,https://b.mp4")
    private String videoUrls;

    @Schema(description = "销售状态：0 下架，1 上架", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "销售状态不能为空")
    private Integer saleStatus;

    @Schema(description = "排序值，越小越靠前", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "排序值不能为空")
    private Integer sort;
}
