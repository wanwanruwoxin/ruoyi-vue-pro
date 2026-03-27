package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Collection;
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

    default PageResult<DsShopOrder> selectAdminPage(PageParam pageParam, String orderNo, Long uid, String payStatus,
                                                     LocalDateTime[] paidAt, Collection<Long> filteredOrderIds) {
        return selectPage(pageParam, new LambdaQueryWrapperX<DsShopOrder>()
                .likeIfPresent(DsShopOrder::getOrderNo, orderNo)
                .eqIfPresent(DsShopOrder::getUid, uid)
                .eqIfPresent(DsShopOrder::getPayStatus, payStatus)
                .betweenIfPresent(DsShopOrder::getPaidAt, paidAt)
                .inIfPresent(DsShopOrder::getId, filteredOrderIds)
                .orderByDesc(DsShopOrder::getId));
    }
}
