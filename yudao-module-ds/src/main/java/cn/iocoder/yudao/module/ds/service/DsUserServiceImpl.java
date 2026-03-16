package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.AUTH_LOGIN_USER_NOT_FOUND;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.USER_MOBILE_EXISTS;

@Service
@Validated
public class DsUserServiceImpl implements DsUserService {

    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Override
    public Long register(String mobile, String nickname, String avatar, String registerChannel) {
        if (dsUserMapper.selectByMobile(mobile) != null) {
            throw exception(USER_MOBILE_EXISTS);
        }
        DsUser dsUser = new DsUser();
        dsUser.setMobile(mobile);
        dsUser.setNickname(StringUtils.hasText(nickname) ? nickname : mobile);
        dsUser.setAvatar(avatar);
        dsUser.setRegisterChannel(registerChannel);
        dsUser.setStatus(CommonStatusEnum.ENABLE.getStatus());
        dsUserMapper.insert(dsUser);
        return dsUser.getId();
    }

    @Override
    public AppDsAuthLoginRespVO login(String mobile) {
        DsUser dsUser = dsUserMapper.selectByMobile(mobile);
        if (dsUser == null) {
            throw exception(AUTH_LOGIN_USER_NOT_FOUND);
        }
        if (CommonStatusEnum.isDisable(dsUser.getStatus())) {
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        OAuth2AccessTokenRespDTO token = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(dsUser.getId())
                .setUserType(UserTypeEnum.MEMBER.getValue())
                .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        return buildLoginResp(token);
    }

    @Override
    public AppDsAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenRespDTO token = oauth2TokenApi.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return buildLoginResp(token);
    }

    @Override
    public void logout(String accessToken) {
        oauth2TokenApi.removeAccessToken(accessToken);
    }

    private AppDsAuthLoginRespVO buildLoginResp(OAuth2AccessTokenRespDTO token) {
        return AppDsAuthLoginRespVO.builder()
                .userId(token.getUserId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresTime(token.getExpiresTime())
                .build();
    }
}
