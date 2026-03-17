package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsPointAccountMapper extends BaseMapperX<DsPointAccount> {

    default DsPointAccount selectByUid(Long uid) {
        return selectOne(DsPointAccount::getUid, uid);
    }
}
