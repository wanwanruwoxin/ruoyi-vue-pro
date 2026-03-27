package cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 平台账户流水分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsPlatformAccountLedgerPageReqVO extends PageParam {

    @Schema(description = "账户编号", example = "1")
    private Long accountId;

    @Schema(description = "变更类型", example = "INCOME")
    private String changeType;

    @Schema(description = "业务类型", example = "SHOP_ORDER_COMMISSION")
    private String bizType;

    @Schema(description = "业务单号", example = "PAC-202603270001")
    private String bizNo;

    @Schema(description = "订单编号", example = "SO202603270001")
    private String orderNo;

    @Schema(description = "发生时间", example = "[2026-03-01 00:00:00, 2026-03-31 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] occurredAt;
}
