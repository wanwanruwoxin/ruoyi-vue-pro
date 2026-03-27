package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductRecommendTrace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsProductRecommendTraceMapper extends BaseMapperX<DsProductRecommendTrace> {

    default DsProductRecommendTrace selectLatestBoundByOrderItemId(Long orderItemId) {
        return selectOne(new LambdaQueryWrapperX<DsProductRecommendTrace>()
                .eq(DsProductRecommendTrace::getBindOrderItemId, orderItemId)
                .orderByDesc(DsProductRecommendTrace::getId)
                .last("LIMIT 1"));
    }
}
