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

@TableName("ds_point_ledger")
@KeySequence("ds_point_ledger_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsPointLedger extends TenantBaseDO {

    @TableId
    private Long id;
    private Long uid;
    private String changeType;
    private BigDecimal points;
    private BigDecimal balanceAfter;
    private String bizType;
    private String bizNo;
    private Long sourceUid;
    private String rewardRuleVersion;
    private LocalDateTime occurredAt;
}
