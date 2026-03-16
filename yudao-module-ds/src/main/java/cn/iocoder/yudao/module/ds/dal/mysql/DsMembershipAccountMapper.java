package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsMembershipAccountMapper extends BaseMapperX<DsMembershipAccount> {

    default DsMembershipAccount selectByUid(Long uid) {
        return selectOne(DsMembershipAccount::getUid, uid);
    }
}
