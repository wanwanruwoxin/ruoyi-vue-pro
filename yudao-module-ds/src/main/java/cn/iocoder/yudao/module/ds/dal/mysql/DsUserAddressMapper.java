package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsUserAddressMapper extends BaseMapperX<DsUserAddress> {

    default List<DsUserAddress> selectListByUid(Long uid) {
        return selectList(new LambdaQueryWrapperX<DsUserAddress>()
                .eq(DsUserAddress::getUid, uid)
                .orderByDesc(DsUserAddress::getIsDefault)
                .orderByDesc(DsUserAddress::getId));
    }

    default DsUserAddress selectByIdAndUid(Long id, Long uid) {
        return selectOne(DsUserAddress::getId, id, DsUserAddress::getUid, uid);
    }

    default DsUserAddress selectDefaultByUid(Long uid) {
        return selectOne(DsUserAddress::getUid, uid, DsUserAddress::getIsDefault, 1);
    }

    default int deleteByUid(Long uid) {
        return delete(new LambdaQueryWrapperX<DsUserAddress>()
                .eq(DsUserAddress::getUid, uid));
    }
}
