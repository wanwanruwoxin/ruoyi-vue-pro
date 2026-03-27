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

@TableName("ds_shop_order_item")
@KeySequence("ds_shop_order_item_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsShopOrderItem extends TenantBaseDO {

    @TableId
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long uid;
    private Long shopId;
    private Long productId;
    private String productName;
    private BigDecimal priceAmount;
    private Integer quantity;
    private BigDecimal lineAmount;
    private LocalDateTime paidAt;
}
