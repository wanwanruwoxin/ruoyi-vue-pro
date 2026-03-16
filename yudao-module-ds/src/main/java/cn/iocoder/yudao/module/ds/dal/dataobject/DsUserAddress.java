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

@TableName("ds_user_address")
@KeySequence("ds_user_address_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsUserAddress extends TenantBaseDO {

    @TableId
    private Long id;
    private Long uid;
    private String receiverName;
    private String receiverMobile;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
}
