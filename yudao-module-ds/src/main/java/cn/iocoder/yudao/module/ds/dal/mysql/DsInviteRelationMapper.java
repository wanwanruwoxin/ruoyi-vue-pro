package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsInviteRelation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsInviteRelationMapper extends BaseMapperX<DsInviteRelation> {

    default DsInviteRelation selectByInviteeId(Long inviteeId) {
        return selectOne(DsInviteRelation::getInviteeId, inviteeId);
    }
}
