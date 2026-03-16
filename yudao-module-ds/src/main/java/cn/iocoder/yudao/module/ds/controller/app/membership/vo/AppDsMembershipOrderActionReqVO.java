package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "用户 APP - DS 会员订单动作 Request VO")
@Data
public class AppDsMembershipOrderActionReqVO {

    @Schema(description = "会员订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "M67f0b13ea8df4dc5b5da5f888f25c395")
    @NotBlank(message = "会员订单号不能为空")
    private String orderNo;
}
