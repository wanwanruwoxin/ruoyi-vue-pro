package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteLinkRespVO;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteScanRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsInviteRelationMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.INVITE_BIND_SELF;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.INVITE_BIND_LOOP;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.INVITE_RELATION_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.INVITER_NOT_EXISTS;

@Service
@Validated
public class DsInviteRelationServiceImpl implements DsInviteRelationService {

    private static final String INVITE_LINK_TEMPLATE = "/pages/auth/register?inviterId=%d";
    private static final int DIRECT_LEVEL = 1;

    @Resource
    private DsInviteRelationMapper dsInviteRelationMapper;
    @Resource
    private DsUserMapper dsUserMapper;

    @Override
    public AppDsInviteLinkRespVO getInviteLink(Long inviterId) {
        validateInviterExists(inviterId);
        String inviteLink = buildInviteLink(inviterId);
        return AppDsInviteLinkRespVO.builder()
                .inviterId(inviterId)
                .inviteLink(inviteLink)
                .inviteQrCodeContent(inviteLink)
                .build();
    }

    @Override
    public AppDsInviteScanRespVO scanInvite(Long inviterId, String mobile) {
        validateInviterExists(inviterId);
        DsUser dsUser = mobile == null ? null : dsUserMapper.selectByMobile(mobile);
        boolean accountExists = dsUser != null;
        return AppDsInviteScanRespVO.builder()
                .inviterId(inviterId)
                .inviteLink(buildInviteLink(inviterId))
                .accountExists(accountExists)
                .message(accountExists ? "已存在账号" : "可继续注册")
                .bindStrategy(accountExists ? "LOGIN_BIND_REQUIRED" : "REGISTER_AUTO_BIND")
                .build();
    }

    @Override
    public void bindInviteRelation(Long inviterId, Long inviteeId) {
        validateInviterExists(inviterId);
        if (inviterId.equals(inviteeId)) {
            throw exception(INVITE_BIND_SELF);
        }
        if (hasInviterBound(inviteeId)) {
            throw exception(INVITE_RELATION_EXISTS);
        }
        validateNoInviteLoop(inviterId, inviteeId);
        Long currentInviterId = inviterId;
        int currentLevel = DIRECT_LEVEL;
        while (currentInviterId != null) {
            if (inviteeId.equals(currentInviterId)) {
                break;
            }
            createRelationIfAbsent(currentInviterId, inviteeId, currentLevel);
            DsInviteRelation parentRelation = dsInviteRelationMapper.selectByInviteeIdAndLevel(currentInviterId, DIRECT_LEVEL);
            currentInviterId = parentRelation == null ? null : parentRelation.getInviterId();
            currentLevel++;
        }
    }

    @Override
    public boolean hasInviterBound(Long inviteeId) {
        return dsInviteRelationMapper.selectByInviteeId(inviteeId) != null;
    }

    @Override
    public Long getInviterIdByInviteeId(Long inviteeId) {
        DsInviteRelation relation = dsInviteRelationMapper.selectByInviteeId(inviteeId);
        return relation == null ? null : relation.getInviterId();
    }

    @Override
    public Long getInviterIdByInviteeIdAndLevel(Long inviteeId, Integer level) {
        DsInviteRelation relation = dsInviteRelationMapper.selectByInviteeIdAndLevel(inviteeId, level);
        return relation == null ? null : relation.getInviterId();
    }

    @Override
    public List<Long> getDirectInviteeIds(Long inviterId) {
        return dsInviteRelationMapper.selectListByInviterIdAndLevel(inviterId, DIRECT_LEVEL).stream()
                .map(DsInviteRelation::getInviteeId)
                .collect(Collectors.toList());
    }

    private void validateInviterExists(Long inviterId) {
        if (dsUserMapper.selectById(inviterId) == null) {
            throw exception(INVITER_NOT_EXISTS);
        }
    }

    private String buildInviteLink(Long inviterId) {
        return String.format(INVITE_LINK_TEMPLATE, inviterId);
    }

    private void validateNoInviteLoop(Long inviterId, Long inviteeId) {
        Long currentInviterId = inviterId;
        while (currentInviterId != null) {
            if (currentInviterId.equals(inviteeId)) {
                throw exception(INVITE_BIND_LOOP);
            }
            DsInviteRelation currentRelation = dsInviteRelationMapper.selectByInviteeIdAndLevel(currentInviterId, DIRECT_LEVEL);
            currentInviterId = currentRelation == null ? null : currentRelation.getInviterId();
        }
    }

    private void createRelationIfAbsent(Long inviterId, Long inviteeId, Integer level) {
        DsInviteRelation existed = dsInviteRelationMapper.selectByInviterIdAndInviteeIdAndLevel(inviterId, inviteeId, level);
        if (existed != null) {
            return;
        }
        DsInviteRelation relation = new DsInviteRelation();
        relation.setInviterId(inviterId);
        relation.setInviteeId(inviteeId);
        relation.setLevel(level);
        relation.setSourceQrCode(buildInviteLink(inviterId));
        relation.setBindStatus(1);
        dsInviteRelationMapper.insert(relation);
    }
}
