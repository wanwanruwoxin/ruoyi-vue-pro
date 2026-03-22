package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DsShopOrderMapper extends BaseMapperX<DsShopOrder> {

    default DsShopOrder selectByOrderNoAndUid(String orderNo, Long uid) {
        return selectOne(DsShopOrder::getOrderNo, orderNo, DsShopOrder::getUid, uid);
    }

    default List<DsShopOrder> selectListByUid(Long uid) {
        return selectList(new LambdaQueryWrapperX<DsShopOrder>()
                .eq(DsShopOrder::getUid, uid)
                .orderByDesc(DsShopOrder::getId));
    }
}
