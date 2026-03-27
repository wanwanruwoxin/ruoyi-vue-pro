package cn.iocoder.yudao.module.ds.controller.admin.recommend.vo;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductRecommendTrace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 商品推荐跟踪 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class DsProductRecommendTraceRespVO extends DsProductRecommendTrace {

    @Schema(description = "店铺名称", example = "三三生活")
    private String shopName;

    @Schema(description = "商品名称", example = "山茶花保湿面膜")
    private String productName;

    @Schema(description = "邀请人昵称", example = "小李")
    private String inviterNickname;

    @Schema(description = "邀请人手机号", example = "13800000000")
    private String inviterMobile;

    @Schema(description = "被邀请人昵称", example = "小王")
    private String inviteeNickname;

    @Schema(description = "被邀请人手机号", example = "13900000000")
    private String inviteeMobile;
}
