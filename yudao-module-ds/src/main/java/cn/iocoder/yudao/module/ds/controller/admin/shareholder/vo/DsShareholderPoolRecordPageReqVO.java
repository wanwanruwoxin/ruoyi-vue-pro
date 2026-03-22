package cn.iocoder.yudao.module.ds.controller.admin.shareholder.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 股东池记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsShareholderPoolRecordPageReqVO extends PageParam {

    @Schema(description = "来源用户编号", example = "100")
    private Long sourceUid;

    @Schema(description = "来源类型", example = "MEMBERSHIP_ORDER_PROFIT")
    private String sourceType;

    @Schema(description = "来源订单号", example = "MO202603220001")
    private String sourceOrderNo;

    @Schema(description = "结算月份", example = "2026-03")
    private String settleMonth;

    @Schema(description = "结算状态", example = "PENDING")
    private String settleStatus;

    @Schema(description = "发生时间", example = "[2026-03-01 00:00:00, 2026-03-31 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] occurredAt;
}
