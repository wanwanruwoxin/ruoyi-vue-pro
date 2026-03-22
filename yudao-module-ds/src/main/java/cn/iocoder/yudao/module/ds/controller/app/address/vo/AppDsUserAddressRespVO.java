package cn.iocoder.yudao.module.ds.controller.app.address.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - DS 收货地址 Response VO")
@Data
public class AppDsUserAddressRespVO {

    @Schema(description = "地址编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "收货人", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String receiverName;

    @Schema(description = "收货手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    private String receiverMobile;

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "浙江省")
    private String province;

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "杭州市")
    private String city;

    @Schema(description = "区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "西湖区")
    private String district;

    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "文三路 10 号")
    private String detailAddress;

    @Schema(description = "是否默认地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer isDefault;
}
