package cn.iocoder.yudao.module.ds.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - DS 商城订单创建 Request VO")
@Data
public class AppDsShopOrderCreateReqVO {

    @Schema(description = "购买商品列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "购买商品不能为空")
    @Valid
    private List<Item> items;

    @Schema(description = "用户 APP - DS 商城订单商品项")
    @Data
    public static class Item {

        @Schema(description = "商品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "商品编号不能为空")
        private Long productId;

        @Schema(description = "购买数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
        @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量必须大于 0")
        private Integer quantity;
    }
}
