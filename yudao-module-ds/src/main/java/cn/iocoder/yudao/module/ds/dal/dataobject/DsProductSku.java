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

@TableName("ds_product_sku")
@KeySequence("ds_product_sku_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsProductSku extends TenantBaseDO {

    @TableId
    private Long id;
    private Long spuId;
    private String name;
    private String propertiesJson;
    private BigDecimal priceAmount;
    private BigDecimal marketPrice;
    private BigDecimal costPrice;
    private String barCode;
    private String picUrl;
    private Integer stock;
    private Double weight;
    private Double volume;
    private Integer firstBrokeragePrice;
    private Integer secondBrokeragePrice;
    private Integer salesCount;
    private LocalDateTime saleTime;
}
