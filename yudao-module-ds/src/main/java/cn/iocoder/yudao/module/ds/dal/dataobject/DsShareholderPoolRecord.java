package cn.iocoder.yudao.module.ds.dal.dataobject;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("ds_shareholder_pool_record")
@KeySequence("ds_shareholder_pool_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsShareholderPoolRecord extends TenantBaseDO {

    @TableId
    private Long id;
    private String bizNo;
    private String sourceType;
    private String sourceOrderNo;
    private Long sourceUid;
    private BigDecimal profitAmount;
    private BigDecimal poolRate;
    private BigDecimal poolAmount;
    private String settleMonth;
    private String settleStatus;
    private String settleBatchNo;
    private LocalDateTime settledAt;
    private LocalDateTime occurredAt;
}
