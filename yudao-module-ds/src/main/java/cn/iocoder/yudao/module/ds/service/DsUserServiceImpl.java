package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.ds.controller.admin.user.vo.DsUserUpdateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthRegisterReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsUserAvatarUpdateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsUserNicknameUpdateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsUserPasswordUpdateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsUserProfileRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipAccountMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointAccountMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserAddressMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.enums.DsShopAuditStatusEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.*;

@Slf4j
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
    private DsMembershipAccountMapper dsMembershipAccountMapper;
    @Resource
    private DsPointAccountMapper dsPointAccountMapper;
    @Resource
    private DsShopMapper dsShopMapper;
//    @Resource
//    private SmsCodeApi smsCodeApi;

//    @Override
//    public void sendRegisterSmsCode(String mobile) {
//        SmsCodeSendReqDTO reqDTO = new SmsCodeSendReqDTO();
//        reqDTO.setMobile(mobile);
//        reqDTO.setScene(SmsSceneEnum.MEMBER_LOGIN.getScene());
//        reqDTO.setCreateIp(ServletUtils.getClientIP());
//        smsCodeApi.sendSmsCode(reqDTO);
//    }

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
    public AppDsAuthLoginRespVO login(String mobile, String password, Integer userType) {
        DsUser dsUser = dsUserMapper.selectByMobile(mobile);
        if (dsUser == null || !passwordEncoder.matches(password, dsUser.getPassword())) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(dsUser.getStatus())) {
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        if (UserTypeEnum.ADMIN.getValue().equals(userType)) {
            DsShop shop = dsShopMapper.selectByUid(dsUser.getId());
            if (shop == null || !DsShopAuditStatusEnum.APPROVED.getStatus().equals(shop.getStatus())) {
                throw exception(AUTH_ADMIN_NOT_OPENED);
            }
        }
        OAuth2AccessTokenRespDTO token = oauth2TokenApi.createAccessToken(new OAuth2AccessTokenCreateReqDTO()
                .setUserId(dsUser.getId())
                .setUserType(userType)
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

    @Override
    public AppDsUserProfileRespVO getUserProfile(Long userId) {
        DsUser dsUser = dsUserMapper.selectById(userId);
        if (dsUser == null) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        AppDsUserProfileRespVO respVO = new AppDsUserProfileRespVO();
        respVO.setUserId(dsUser.getId());
        respVO.setNickname(dsUser.getNickname());
        respVO.setAvatar(dsUser.getAvatar());
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserNickname(Long userId, AppDsUserNicknameUpdateReqVO reqVO) {
        DsUser dsUser = dsUserMapper.selectById(userId);
        if (dsUser == null) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        dsUser.setNickname(reqVO.getNickname());
        dsUserMapper.updateById(dsUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAvatar(Long userId, AppDsUserAvatarUpdateReqVO reqVO) {
        DsUser dsUser = dsUserMapper.selectById(userId);
        if (dsUser == null) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        dsUser.setAvatar(reqVO.getAvatar());
        dsUserMapper.updateById(dsUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPassword(Long userId, AppDsUserPasswordUpdateReqVO reqVO) {
        DsUser dsUser = dsUserMapper.selectById(userId);
        if (dsUser == null) {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!passwordEncoder.matches(reqVO.getOldPassword(), dsUser.getPassword())) {
            throw exception(USER_PASSWORD_OLD_INCORRECT);
        }
        dsUser.setPassword(passwordEncoder.encode(reqVO.getNewPassword()));
        dsUserMapper.updateById(dsUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAdminUser(DsUserUpdateReqVO reqVO) {
        BigDecimal availablePoints = reqVO.getAvailablePoints() == null ? BigDecimal.ZERO : reqVO.getAvailablePoints();
        DsUser user = dsUserMapper.selectById(reqVO.getId());
        if (user == null) {
            return false;
        }
        user.setNickname(reqVO.getNickname());
        user.setAvatar(reqVO.getAvatar());
        user.setStatus(reqVO.getStatus());
        dsUserMapper.updateById(user);

        DsMembershipAccount account = dsMembershipAccountMapper.selectByUid(reqVO.getId());
        if (account == null) {
            account = new DsMembershipAccount();
            account.setUid(reqVO.getId());
            account.setCurrentPlanCode(reqVO.getCurrentPlanCode());
            account.setMemberStatus(reqVO.getMemberStatus());
            account.setTeamLeader(reqVO.getTeamLeader());
            account.setShareholder(reqVO.getShareholder());
            dsMembershipAccountMapper.insert(account);
        } else {
            account.setCurrentPlanCode(reqVO.getCurrentPlanCode());
            account.setMemberStatus(reqVO.getMemberStatus());
            account.setTeamLeader(reqVO.getTeamLeader());
            account.setShareholder(reqVO.getShareholder());
            dsMembershipAccountMapper.updateById(account);
        }

        DsPointAccount pointAccount = dsPointAccountMapper.selectByUid(reqVO.getId());
        if (pointAccount == null) {
            pointAccount = new DsPointAccount();
            pointAccount.setUid(reqVO.getId());
            pointAccount.setAvailablePoints(availablePoints);
            pointAccount.setFrozenPoints(BigDecimal.ZERO);
            pointAccount.setTotalEarnedPoints(availablePoints);
            pointAccount.setTotalSpentPoints(BigDecimal.ZERO);
            dsPointAccountMapper.insert(pointAccount);
        } else {
            pointAccount.setAvailablePoints(availablePoints);
            dsPointAccountMapper.updateById(pointAccount);
        }
        return true;
    }

    private AppDsAuthLoginRespVO buildLoginResp(OAuth2AccessTokenRespDTO token) {
        DsUser dsUser = dsUserMapper.selectById(token.getUserId());
        return AppDsAuthLoginRespVO.builder()
                .userId(token.getUserId())
                .nickname(dsUser != null ? dsUser.getNickname() : null)
                .avatar(dsUser != null ? dsUser.getAvatar() : null)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresTime(token.getExpiresTime())
                .build();
    }
}
