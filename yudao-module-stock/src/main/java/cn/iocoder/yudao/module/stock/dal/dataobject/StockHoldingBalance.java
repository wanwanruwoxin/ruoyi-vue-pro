package cn.iocoder.yudao.module.stock.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("stock_holding_balance")
@KeySequence("stock_holding_balance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockHoldingBalance extends BaseDO {
    @TableId
    private Long id;
    private Long uid;
    private Long accountId;
    private Long assetId;
    private double quantity;
    private double avgCost;
    private double costAmount;
    private double marketPrice;
    private double marketValue;
    private double unrealizedPnl;
    private double unrealizedPnlRate;
    private Long updatedByTxnId;
}
