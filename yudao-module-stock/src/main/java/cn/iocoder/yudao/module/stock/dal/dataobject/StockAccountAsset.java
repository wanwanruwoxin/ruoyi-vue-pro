package cn.iocoder.yudao.module.stock.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.stock.enums.StockStatus;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("stock_account_asset")
@KeySequence("stock_account_asset_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAccountAsset extends BaseDO {
    @TableId
    private Long id;
    private Long uid;
    private Long accountId;
    private Long assetId;
    private int initialQuantity;
    private double initialPrice;
    private double initialCost;
    private int targetMultiple;
    private int targetMarketValue;
    private LocalDateTime targetDate;
    private StockStatus status;
}
