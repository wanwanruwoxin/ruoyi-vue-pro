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

import java.time.LocalDateTime;

@TableName("ds_product_recommend_trace")
@KeySequence("ds_product_recommend_trace_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsProductRecommendTrace extends TenantBaseDO {

    @TableId
    private Long id;
    private String traceNo;
    private Long inviterId;
    private Long inviteeId;
    private Long shopId;
    private Long productId;
    private String recommendScene;
    private Integer recommendStatus;
    private LocalDateTime recommendedAt;
    private LocalDateTime expireAt;
    private String bindOrderNo;
    private Long bindOrderItemId;
}
