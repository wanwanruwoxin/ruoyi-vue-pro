package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsPlatformAccountMapper extends BaseMapperX<DsPlatformAccount> {

    default DsPlatformAccount selectByAccountCode(String accountCode) {
        return selectOne(DsPlatformAccount::getAccountCode, accountCode);
    }
}
