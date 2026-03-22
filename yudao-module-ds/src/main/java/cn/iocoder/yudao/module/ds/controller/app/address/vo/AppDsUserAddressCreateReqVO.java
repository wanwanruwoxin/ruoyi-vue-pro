package cn.iocoder.yudao.module.ds.controller.app.address.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - DS 收货地址创建 Request VO")
@Data
public class AppDsUserAddressCreateReqVO {

    @Schema(description = "收货人", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotBlank(message = "收货人不能为空")
    @Length(min = 1, max = 64, message = "收货人长度为 1-64 位")
    private String receiverName;

    @Schema(description = "收货手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "收货手机号不能为空")
    @Mobile
    private String receiverMobile;

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "浙江省")
    @NotBlank(message = "省份不能为空")
    private String province;

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "杭州市")
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "区县", requiredMode = Schema.RequiredMode.REQUIRED, example = "西湖区")
    @NotBlank(message = "区县不能为空")
    private String district;

    @Schema(description = "详细地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "文三路 10 号")
    @NotBlank(message = "详细地址不能为空")
    @Length(min = 1, max = 255, message = "详细地址长度为 1-255 位")
    private String detailAddress;

    @Schema(description = "是否默认地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean defaultAddress;
}
