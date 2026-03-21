package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - DS 会员奖励规则 Response VO")
@Data
public class AppDsMembershipRewardRuleRespVO {

    @Schema(description = "规则版本", requiredMode = Schema.RequiredMode.REQUIRED, example = "INVITE_REWARD_V1")
    private String ruleVersion;

    @Schema(description = "规则说明", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ruleDescription;

    @Schema(description = "适用会员方案编码", example = "ADVANCED")
    private String applicablePlanCode;

    @Schema(description = "适用邀请层级", example = "LEVEL_1")
    private String applicableInviterLevel;
}
