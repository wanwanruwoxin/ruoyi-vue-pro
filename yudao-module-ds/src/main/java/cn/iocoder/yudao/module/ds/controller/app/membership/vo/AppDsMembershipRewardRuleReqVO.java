package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - DS 会员奖励规则 Request VO")
@Data
public class AppDsMembershipRewardRuleReqVO {

    @Schema(description = "会员方案编码", example = "ADVANCED")
    private String planCode;
}
