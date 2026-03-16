package cn.iocoder.yudao.module.ds.controller.app.membership.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - DS 会员账户 Response VO")
@Data
public class AppDsMembershipAccountRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long uid;

    @Schema(description = "当前方案编码", example = "NORMAL")
    private String currentPlanCode;

    @Schema(description = "会员状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "ACTIVE")
    private String memberStatus;

    @Schema(description = "生效时间")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;
}
