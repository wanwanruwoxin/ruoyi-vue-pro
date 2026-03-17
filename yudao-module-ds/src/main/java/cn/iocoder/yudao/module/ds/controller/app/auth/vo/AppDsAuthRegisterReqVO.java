package cn.iocoder.yudao.module.ds.controller.app.auth.vo;

import cn.iocoder.yudao.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - DS 注册 Request VO")
@Data
public class AppDsAuthRegisterReqVO {

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotBlank(message = "手机号不能为空")
    @Mobile
    private String mobile;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "小芋")
    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 64, message = "昵称长度为 1-64 位")
    private String nickname;

    @Schema(description = "头像", example = "https://www.iocoder.cn/avatar.png")
    private String avatar;

    @Schema(description = "注册渠道", requiredMode = Schema.RequiredMode.REQUIRED, example = "APP")
    @NotBlank(message = "注册渠道不能为空")
    private String registerChannel;

//    @Schema(description = "短信验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
//    @NotBlank(message = "短信验证码不能为空")
//    @Length(min = 4, max = 8, message = "短信验证码长度为 4-8 位")
//    private String smsCode;

    @Schema(description = "邀请人用户编号", example = "1001")
    private Long inviterId;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 32, message = "密码长度为 6-32 位")
    private String password;

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
}
