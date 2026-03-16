package cn.iocoder.yudao.module.stock.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("stock_pnl_summary_daily")
@KeySequence("stock_pnl_summary_daily_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPnlSummaryDaily extends BaseDO {
    @TableId
    private Long id;
    private Long uid;
    private LocalDateTime summaryDate;
    private double dailyPnl;
    private double cumulativePnl;
}
