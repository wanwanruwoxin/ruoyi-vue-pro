package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteLinkRespVO;
import cn.iocoder.yudao.module.ds.controller.app.invite.vo.AppDsInviteScanRespVO;

public interface DsInviteRelationService {

    AppDsInviteLinkRespVO getInviteLink(Long inviterId);

    AppDsInviteScanRespVO scanInvite(Long inviterId, String mobile);

    void bindInviteRelation(Long inviterId, Long inviteeId);

    boolean hasInviterBound(Long inviteeId);

    Long getInviterIdByInviteeId(Long inviteeId);
}
