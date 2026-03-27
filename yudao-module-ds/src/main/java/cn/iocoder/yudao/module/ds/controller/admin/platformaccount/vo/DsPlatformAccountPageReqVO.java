package cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 平台账户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DsPlatformAccountPageReqVO extends PageParam {

    @Schema(description = "账户编码", example = "MAIN")
    private String accountCode;
}
