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

@TableName("ds_recommend_reward_record")
@KeySequence("ds_recommend_reward_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsRecommendRewardRecord extends TenantBaseDO {

    @TableId
    private Long id;
    private String rewardNo;
    private Long splitId;
    private Long orderId;
    private String orderNo;
    private Long orderItemId;
    private String traceNo;
    private Long inviterId;
    private Long inviteeId;
    private BigDecimal rewardRate;
    private BigDecimal rewardAmount;
    private String grantStatus;
    private String grantError;
    private LocalDateTime grantedAt;
    private LocalDateTime occurredAt;
}
