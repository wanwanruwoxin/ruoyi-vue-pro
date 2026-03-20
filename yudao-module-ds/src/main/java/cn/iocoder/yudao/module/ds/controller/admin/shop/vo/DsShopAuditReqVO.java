package cn.iocoder.yudao.module.ds.controller.admin.shop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "管理后台 - DS 店铺审核 Request VO")
@Data
public class DsShopAuditReqVO {

    @Schema(description = "店铺编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "店铺编号不能为空")
    private Long id;

    @Schema(description = "审核状态（1审核通过 2审核拒绝）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    @Schema(description = "审核备注", example = "资料齐全，审核通过")
    @Length(max = 255, message = "审核备注长度不能超过 255 位")
    private String auditRemark;
}
