package cn.iocoder.yudao.module.stock.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("stock_transaction_ledger")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransactionLedger extends BaseDO {
    @TableId
    private Long id;
    private Long uid;
    private Long accountId;
    private Long assetId;
    private Long assetProfileId;
    private String assetType;
    private String tradeType;
    private LocalDateTime tradeDate;
    private double tradeQuantity;
    private double feeAmount;
    private double tradeAmount;
    private String currency;
    private String sourceType;
    private Long sourceRefId;
    private Long correctedFromTxnId;
}
