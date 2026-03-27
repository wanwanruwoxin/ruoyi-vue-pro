package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

@Mapper
public interface DsShopOrderItemMapper extends BaseMapperX<DsShopOrderItem> {

    default List<Long> selectDistinctOrderIdsByShopId(Long shopId) {
        LambdaQueryWrapperX<DsShopOrderItem> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(DsShopOrderItem::getShopId, shopId);
        queryWrapper.groupBy(DsShopOrderItem::getOrderId);
        queryWrapper.select(DsShopOrderItem::getOrderId);
        return selectList(queryWrapper)
                .stream()
                .map(DsShopOrderItem::getOrderId)
                .filter(Objects::nonNull)
                .toList();
    }

    default List<DsShopOrderItem> selectListByOrderId(Long orderId, Long shopId) {
        return selectList(new LambdaQueryWrapperX<DsShopOrderItem>()
                .eq(DsShopOrderItem::getOrderId, orderId)
                .eqIfPresent(DsShopOrderItem::getShopId, shopId)
                .orderByAsc(DsShopOrderItem::getId));
    }

    default List<Long> selectOrderItemIdsByShopId(Long shopId) {
        return selectList(new LambdaQueryWrapperX<DsShopOrderItem>()
                .eq(DsShopOrderItem::getShopId, shopId)
                .select(DsShopOrderItem::getId))
                .stream()
                .map(DsShopOrderItem::getId)
                .filter(Objects::nonNull)
                .toList();
    }
}
