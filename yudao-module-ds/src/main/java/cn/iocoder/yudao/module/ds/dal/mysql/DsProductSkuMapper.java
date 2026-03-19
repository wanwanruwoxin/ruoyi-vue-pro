package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuPageReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsProductSkuMapper extends BaseMapperX<DsProductSku> {

    default PageResult<DsProductSku> selectPage(DsProductSkuPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DsProductSku>()
                .eqIfPresent(DsProductSku::getSpuId, reqVO.getSpuId())
                .likeIfPresent(DsProductSku::getName, reqVO.getName())
                .likeIfPresent(DsProductSku::getBarCode, reqVO.getBarCode())
                .orderByDesc(DsProductSku::getId));
    }
}
