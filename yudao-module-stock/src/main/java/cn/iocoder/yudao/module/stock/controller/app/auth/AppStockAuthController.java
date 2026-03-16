package cn.iocoder.yudao.module.stock.controller.app.auth;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.stock.controller.app.auth.vo.AppStockAuthLoginReqVO;
import cn.iocoder.yudao.module.stock.controller.app.auth.vo.AppStockAuthLoginRespVO;
import cn.iocoder.yudao.module.stock.controller.app.auth.vo.AppStockAuthRegisterReqVO;
import cn.iocoder.yudao.module.stock.service.StockUserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 股票认证")
@RestController
@RequestMapping("/stock/auth")
@Validated
public class AppStockAuthController {

    @Resource
    private StockUserService stockUserService;
    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/register")
    @Operation(summary = "注册")
    @PermitAll
    public CommonResult<Long> register(@RequestBody @Valid AppStockAuthRegisterReqVO reqVO) {
        return success(stockUserService.register(reqVO.getPhone(), reqVO.getEmail(), reqVO.getPassword()));
    }

    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    @PermitAll
    public CommonResult<AppStockAuthLoginRespVO> login(@RequestBody @Valid AppStockAuthLoginReqVO reqVO) {
        return success(stockUserService.login(reqVO.getAccount(), reqVO.getPassword()));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出系统")
    @PermitAll
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            stockUserService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @PermitAll
    public CommonResult<AppStockAuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(stockUserService.refreshToken(refreshToken));
    }
}
