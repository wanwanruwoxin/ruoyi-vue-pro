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

    default int selectCountByUidsAndPlanCode(List<Long> uids, String planCode) {
        if (uids == null || uids.isEmpty()) {
            return 0;
        }
        Long count = selectCount(new LambdaQueryWrapperX<DsMembershipAccount>()
                .in(DsMembershipAccount::getUid, uids)
                .eq(DsMembershipAccount::getCurrentPlanCode, planCode));
        return count == null ? 0 : count.intValue();
    }

    default int deleteByUid(Long uid) {
        return delete(new LambdaQueryWrapperX<DsMembershipAccount>()
                .eq(DsMembershipAccount::getUid, uid));
    }
}
