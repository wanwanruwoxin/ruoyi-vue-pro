package cn.iocoder.yudao.module.stock.service;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.stock.controller.app.auth.vo.AppStockAuthLoginRespVO;
import cn.iocoder.yudao.module.stock.dal.dataobject.StockUser;
import cn.iocoder.yudao.module.stock.dal.mysql.StockUserMapper;
import cn.iocoder.yudao.module.stock.enums.StockStatus;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.stock.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static cn.iocoder.yudao.module.stock.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;
import static cn.iocoder.yudao.module.stock.enums.ErrorCodeConstants.USER_EMAIL_EXISTS;
import static cn.iocoder.yudao.module.stock.enums.ErrorCodeConstants.USER_PHONE_EXISTS;

@Service
@Validated
public class StockUserServiceImpl implements StockUserService {

    @Resource
    private StockUserMapper stockUserMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Override
    public Long register(String phone, String email, String password) {
        if (StringUtils.hasText(phone) && stockUserMapper.selectByPhone(phone) != null) {
            throw exception(USER_PHONE_EXISTS);
        }
        if (StringUtils.hasText(email) && stockUserMapper.selectByEmail(email) != null) {
            throw exception(USER_EMAIL_EXISTS);
        }
        StockUser stockUser = new StockUser();
        stockUser.setPhone(phone);
        stockUser.setEmail(email);
        stockUser.setStatus(StockStatus.ACTIVE.name());
        stockUser.setPassword(passwordEncoder.encode(password));
        stockUserMapper.insert(stockUser);
        return stockUser.getId();
    }

    @Override
    public AppStockAuthLoginRespVO login(String account, String password) {
        StockUser stockUser = stockUserMapper.selectByPhone(account);
        if (stockUser == null) {
            stockUser = stockUserMapper.selectByEmail(account);
        }
        if (stockUser == null || !passwordEncoder.matches(password, stockUser.getPassword())) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!StockStatus.ACTIVE.name().equals(stockUser.getStatus())) {
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        OAuth2AccessTokenRespDTO token = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(stockUser.getId())
                .setUserType(UserTypeEnum.MEMBER.getValue())
                .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        return buildLoginResp(token);
    }

    @Override
    public AppStockAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenRespDTO token = oauth2TokenApi.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return buildLoginResp(token);
    }

    @Override
    public void logout(String accessToken) {
        oauth2TokenApi.removeAccessToken(accessToken);
    }

    private AppStockAuthLoginRespVO buildLoginResp(OAuth2AccessTokenRespDTO token) {
        return AppStockAuthLoginRespVO.builder()
                .userId(token.getUserId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresTime(token.getExpiresTime())
                .build();
    }
}
