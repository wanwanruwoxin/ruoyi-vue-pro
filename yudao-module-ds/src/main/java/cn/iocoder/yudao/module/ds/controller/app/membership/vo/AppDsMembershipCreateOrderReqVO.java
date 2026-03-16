package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - DS 会员订单创建 Request VO")
@Data
public class AppDsMembershipCreateOrderReqVO {

    @Schema(description = "会员方案编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "会员方案编号不能为空")
    private Long planId;
}
