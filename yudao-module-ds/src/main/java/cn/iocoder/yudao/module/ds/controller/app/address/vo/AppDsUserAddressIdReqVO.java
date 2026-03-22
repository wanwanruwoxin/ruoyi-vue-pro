package cn.iocoder.yudao.module.ds.controller.app.address.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - DS 收货地址编号 Request VO")
@Data
public class AppDsUserAddressIdReqVO {

    @Schema(description = "地址编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "地址编号不能为空")
    private Long id;
}
