package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsMembershipOrderMapper extends BaseMapperX<DsMembershipOrder> {

    default DsMembershipOrder selectByOrderNoAndUid(String orderNo, Long uid) {
        return selectOne(DsMembershipOrder::getOrderNo, orderNo, DsMembershipOrder::getUid, uid);
    }

    default List<DsMembershipOrder> selectListByUid(Long uid) {
        return selectList(new LambdaQueryWrapperX<DsMembershipOrder>()
                .eq(DsMembershipOrder::getUid, uid)
                .orderByDesc(DsMembershipOrder::getId));
    }
}
