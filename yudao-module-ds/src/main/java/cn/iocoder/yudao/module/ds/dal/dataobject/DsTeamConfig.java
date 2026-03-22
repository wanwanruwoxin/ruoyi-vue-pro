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

@TableName("ds_team_config")
@KeySequence("ds_team_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DsTeamConfig extends TenantBaseDO {

    @TableId
    private Long id;
    private String configKey;
    private String configName;
    private String configValue;
    private String valueType;
    private String configGroup;
    private Integer sort;
    private Integer status;
    private String remark;
}
