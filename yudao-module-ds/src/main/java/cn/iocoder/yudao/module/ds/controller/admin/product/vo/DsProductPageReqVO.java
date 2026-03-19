package cn.iocoder.yudao.module.ds.controller.admin.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - DS 商品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsProductPageReqVO extends PageParam {

    public static final Integer FOR_SALE = 0;

    public static final Integer IN_WAREHOUSE = 1;

    public static final Integer SOLD_OUT = 2;

    public static final Integer ALERT_STOCK = 3;

    public static final Integer RECYCLE_BIN = 4;

    @Schema(description = "店铺编号", example = "1")
    private Long shopId;

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "商品名称关键字", example = "代餐")
    private String productName;

    @Schema(description = "前端请求的 tab 类型", example = "0")
    private Integer tabType;

    @Schema(description = "销售状态：0 下架，1 上架", example = "1")
    private Integer saleStatus;

    @Schema(description = "创建时间", example = "[2026-03-01 00:00:00, 2026-03-31 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}
