package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthRegisterReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserAddressMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.system.api.sms.SmsCodeApi;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeSendReqDTO;
import cn.iocoder.yudao.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import cn.iocoder.yudao.module.system.enums.sms.SmsSceneEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.USER_MOBILE_EXISTS;

@Service
@Validated
public class DsUserServiceImpl implements DsUserService {

    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private DsUserAddressMapper dsUserAddressMapper;
    @Resource
    private DsInviteRelationService dsInviteRelationService;
    @Resource
    private SmsCodeApi smsCodeApi;

    @Override
    public void sendRegisterSmsCode(String mobile) {
        SmsCodeSendReqDTO reqDTO = new SmsCodeSendReqDTO();
        reqDTO.setMobile(mobile);
        reqDTO.setScene(SmsSceneEnum.MEMBER_LOGIN.getScene());
        reqDTO.setCreateIp(ServletUtils.getClientIP());
        smsCodeApi.sendSmsCode(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(AppDsAuthRegisterReqVO reqVO) {
        if (dsUserMapper.selectByMobile(reqVO.getMobile()) != null) {
            throw exception(USER_MOBILE_EXISTS);
        }
//        SmsCodeUseReqDTO smsCodeReqDTO = new SmsCodeUseReqDTO();
//        smsCodeReqDTO.setMobile(reqVO.getMobile());
//        smsCodeReqDTO.setCode(reqVO.getSmsCode());
//        smsCodeReqDTO.setScene(SmsSceneEnum.MEMBER_LOGIN.getScene());
//        smsCodeReqDTO.setUsedIp(ServletUtils.getClientIP());
//        smsCodeApi.useSmsCode(smsCodeReqDTO);
        DsUser dsUser = new DsUser();
        dsUser.setMobile(reqVO.getMobile());
        dsUser.setNickname(StringUtils.hasText(reqVO.getNickname()) ? reqVO.getNickname() : reqVO.getMobile());
        dsUser.setPassword(passwordEncoder.encode(reqVO.getPassword()));
        dsUser.setAvatar(reqVO.getAvatar());
        dsUser.setRegisterChannel(reqVO.getRegisterChannel());
        dsUser.setStatus(CommonStatusEnum.ENABLE.getStatus());
        dsUserMapper.insert(dsUser);
        DsUserAddress userAddress = new DsUserAddress();
        userAddress.setUid(dsUser.getId());
        userAddress.setReceiverName(reqVO.getReceiverName());
        userAddress.setReceiverMobile(reqVO.getReceiverMobile());
        userAddress.setProvince(reqVO.getProvince());
        userAddress.setCity(reqVO.getCity());
        userAddress.setDistrict(reqVO.getDistrict());
        userAddress.setDetailAddress(reqVO.getDetailAddress());
        userAddress.setIsDefault(1);
        dsUserAddressMapper.insert(userAddress);
        if (reqVO.getInviterId() != null) {
            dsInviteRelationService.bindInviteRelation(reqVO.getInviterId(), dsUser.getId());
        }
        return dsUser.getId();
    }

    @Override
    public AppDsAuthLoginRespVO login(String mobile, String password) {
        DsUser dsUser = dsUserMapper.selectByMobile(mobile);
        if (dsUser == null || !passwordEncoder.matches(password, dsUser.getPassword())) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
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