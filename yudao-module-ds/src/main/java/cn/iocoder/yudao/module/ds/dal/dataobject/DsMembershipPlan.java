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

@TableName("ds_membership_plan")
@KeySequence("ds_membership_plan_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsMembershipPlan extends TenantBaseDO {

    @TableId
    private Long id;
    private String planCode;
    private String planName;
    private BigDecimal priceAmount;
    private Integer durationDays;
    private Integer status;
}
