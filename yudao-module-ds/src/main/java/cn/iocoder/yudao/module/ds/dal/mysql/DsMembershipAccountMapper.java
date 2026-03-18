package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsMembershipAccountMapper extends BaseMapperX<DsMembershipAccount> {

    default DsMembershipAccount selectByUid(Long uid) {
        return selectOne(DsMembershipAccount::getUid, uid);
    }

    default List<DsMembershipAccount> selectListByShareholderAndMemberStatus(Integer shareholder, String memberStatus) {
        return selectList(new LambdaQueryWrapperX<DsMembershipAccount>()
                .eq(DsMembershipAccount::getShareholder, shareholder)
                .eq(DsMembershipAccount::getMemberStatus, memberStatus));
    }
}
