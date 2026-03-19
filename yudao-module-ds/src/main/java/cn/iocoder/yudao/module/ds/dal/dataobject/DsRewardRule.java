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

@TableName("ds_reward_rule")
@KeySequence("ds_reward_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsRewardRule extends TenantBaseDO {

    @TableId
    private Long id;
    private String ruleVersion;
    private String ruleDescription;
    private String triggerEvent;
    private BigDecimal rewardRate;
    private BigDecimal dailyCapPoints;
    private String applicableInviterLevel;
    private Integer status;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
