package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    default List<DsInviteRelation> selectListByInviterIdAndLevel(Long inviterId, Integer level) {
        return selectList(new LambdaQueryWrapperX<DsInviteRelation>()
                .eq(DsInviteRelation::getInviterId, inviterId)
                .eq(DsInviteRelation::getLevel, level));
    }

    default List<DsInviteRelation> selectListByInviterIdAndMinLevel(Long inviterId, Integer minLevel) {
        return selectList(new LambdaQueryWrapperX<DsInviteRelation>()
                .eq(DsInviteRelation::getInviterId, inviterId)
                .ge(DsInviteRelation::getLevel, minLevel));
    }

    default int deleteByInviterOrInviteeId(Long uid) {
        return delete(new LambdaQueryWrapperX<DsInviteRelation>()
                .eq(DsInviteRelation::getInviterId, uid)
                .or()
                .eq(DsInviteRelation::getInviteeId, uid));
    }
}
