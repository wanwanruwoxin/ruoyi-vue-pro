package cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 平台账户 Response VO")
@Data
public class DsPlatformAccountRespVO {

    @Schema(description = "账户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "账户编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "MAIN")
    private String accountCode;

    @Schema(description = "可用余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal availableAmount;

    @Schema(description = "冻结余额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal frozenAmount;

    @Schema(description = "累计收入", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalIncomeAmount;

    @Schema(description = "累计支出", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalExpenseAmount;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
