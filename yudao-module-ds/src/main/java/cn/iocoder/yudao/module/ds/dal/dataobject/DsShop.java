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

@TableName("ds_shop")
@KeySequence("ds_shop_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsShop extends TenantBaseDO {

    @TableId
    private Long id;
    private Long uid;
    private String shopName;
    private String avatarUrl;
    private String intro;
    private String contactMobile;
    private String shipProvince;
    private String shipCity;
    private String shipDistrict;
    private String shipDetailAddress;
    private Integer status;
    private String auditRemark;
    private Long auditAdminUserId;
    private Long backendAdminUserId;
    private Integer sort;
}
