package cn.iocoder.yudao.module.ds.controller.app.auth;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthRegisterReqVO;
import cn.iocoder.yudao.module.ds.service.DsUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - DS 认证")
@RestController
@RequestMapping("/ds/auth")
@Validated
public class AppDsAuthController {

    @Resource
    private DsUserService dsUserService;
    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/register")
    @Operation(summary = "注册")
    @PermitAll
    public CommonResult<Long> register(@RequestBody @Valid AppDsAuthRegisterReqVO reqVO) {
        return success(dsUserService.register(reqVO.getMobile(), reqVO.getNickname(), reqVO.getAvatar(), reqVO.getRegisterChannel()));
    }

    @PostMapping("/login")
    @Operation(summary = "手机号登录")
    @PermitAll
    public CommonResult<AppDsAuthLoginRespVO> login(@RequestBody @Valid AppDsAuthLoginReqVO reqVO) {
        return success(dsUserService.login(reqVO.getMobile()));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出系统")
    @PermitAll
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            dsUserService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @PermitAll
    public CommonResult<AppDsAuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(dsUserService.refreshToken(refreshToken));
    }
}
