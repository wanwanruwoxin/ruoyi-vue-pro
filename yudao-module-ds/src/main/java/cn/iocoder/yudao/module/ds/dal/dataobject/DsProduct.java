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

@TableName("ds_product")
@KeySequence("ds_product_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsProduct extends TenantBaseDO {

    @TableId
    private Long id;
    private Long shopId;
    private Long categoryId;
    private Long brandId;
    private String productName;
    private String keyword;
    private String introduction;
    private String description;
    private String picUrl;
    private String sliderPicUrls;
    private BigDecimal priceAmount;
    private BigDecimal marketPrice;
    private BigDecimal costPrice;
    private Integer stock;
    private String detailDesc;
    private String imageUrls;
    private String videoUrls;
    private Boolean specType;
    private String deliveryTypes;
    private Long deliveryTemplateId;
    private Integer giveIntegral;
    private Boolean subCommissionType;
    private Integer saleStatus;
    private Integer sort;
    private Integer salesCount;
    private Integer virtualSalesCount;
    private Integer browseCount;
}
