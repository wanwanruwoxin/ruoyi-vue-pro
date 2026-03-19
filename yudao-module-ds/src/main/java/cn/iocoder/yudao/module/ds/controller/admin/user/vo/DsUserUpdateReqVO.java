package cn.iocoder.yudao.module.ds.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 电商用户更新 Request VO")
@Data
public class DsUserUpdateReqVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户编号不能为空")
    private Long id;

    @Schema(description = "昵称", example = "小明")
    private String nickname;

    @Schema(description = "头像", example = "https://cdn.example.com/avatar.png")
    private String avatar;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "会员方案编码", example = "NORMAL")
    private String currentPlanCode;

    @Schema(description = "会员状态", example = "ACTIVE")
    private String memberStatus;

    @Schema(description = "是否团队长", example = "1")
    private Integer teamLeader;

    @Schema(description = "是否股东", example = "1")
    private Integer shareholder;

    @Schema(description = "可用积分", example = "100")
    private BigDecimal availablePoints;
}
