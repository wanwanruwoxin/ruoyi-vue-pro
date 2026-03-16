package cn.iocoder.yudao.module.stock.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.module.stock.enums.StockAssetType;
import cn.iocoder.yudao.module.stock.enums.StockStatus;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("stock_asset")
@KeySequence("stock_asset_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAsset extends BaseDO {
    /**
     * 编号
     */
    @TableId
    private Long id;
    private String symbol;
    private StockAssetType assetType;
    private String market;
    private String currency;
    private StockStatus status;
}
