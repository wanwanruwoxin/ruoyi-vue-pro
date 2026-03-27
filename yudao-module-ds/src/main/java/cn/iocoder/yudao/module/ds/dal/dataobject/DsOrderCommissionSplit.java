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

@TableName("ds_order_commission_split")
@KeySequence("ds_order_commission_split_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsOrderCommissionSplit extends TenantBaseDO {

    @TableId
    private Long id;
    private String splitNo;
    private Long orderId;
    private String orderNo;
    private Long orderItemId;
    private Long shopId;
    private BigDecimal grossAmount;
    private BigDecimal platformRate;
    private BigDecimal platformAmount;
    private BigDecimal rewardShareRate;
    private BigDecimal rewardAmount;
    private BigDecimal platformNetAmount;
    private String settleStatus;
    private LocalDateTime settledAt;
    private LocalDateTime occurredAt;
}
