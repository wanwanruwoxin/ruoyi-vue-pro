package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsInviteRelationMapper extends BaseMapperX<DsInviteRelation> {

    default DsInviteRelation selectByInviteeId(Long inviteeId) {
        return selectByInviteeIdAndLevel(inviteeId, 1);
    }

    default DsInviteRelation selectByInviteeIdAndLevel(Long inviteeId, Integer level) {
        return selectOne(DsInviteRelation::getInviteeId, inviteeId,
                DsInviteRelation::getLevel, level);
    }

    default DsInviteRelation selectByInviterIdAndInviteeIdAndLevel(Long inviterId, Long inviteeId, Integer level) {
        return selectOne(DsInviteRelation::getInviterId, inviterId,
                DsInviteRelation::getInviteeId, inviteeId,
                DsInviteRelation::getLevel, level);
    }
}
