package cn.iocoder.yudao.module.ds.controller.admin.recommend.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 推荐奖励记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsRecommendRewardRecordPageReqVO extends PageParam {

    @Schema(description = "奖励流水号", example = "SRRxxxx")
    private String rewardNo;

    @Schema(description = "订单号", example = "SO202603270001")
    private String orderNo;

    @Schema(description = "跟踪编号", example = "SPTxxxx")
    private String traceNo;

    @Schema(description = "邀请人用户编号", example = "10001")
    private Long inviterId;

    @Schema(description = "被邀请人用户编号", example = "10002")
    private Long inviteeId;

    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "发放状态", example = "GRANTED")
    private String grantStatus;

    @Schema(description = "发生时间范围", example = "[2026-03-01 00:00:00, 2026-03-31 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] occurredAt;
}
