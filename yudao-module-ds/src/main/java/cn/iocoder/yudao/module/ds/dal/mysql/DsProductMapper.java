package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsProductMapper extends BaseMapperX<DsProduct> {

    default PageResult<DsProduct> selectMyPage(Long shopId, AppDsMyProductPageReqVO reqVO) {
        LambdaQueryWrapperX<DsProduct> wrapper = new LambdaQueryWrapperX<DsProduct>();
        wrapper.eq(DsProduct::getShopId, shopId);
        wrapper.likeIfPresent(DsProduct::getProductName, reqVO.getKeyword());
        wrapper.eqIfPresent(DsProduct::getSaleStatus, reqVO.getSaleStatus());
        wrapper.orderByAsc(DsProduct::getSort);
        wrapper.orderByDesc(DsProduct::getId);
        applySort(wrapper, reqVO.getSortType());
        return selectPage(reqVO, wrapper);
    }

    default PageResult<DsProduct> selectShelfPage(Long shopId, AppDsProductListReqVO reqVO) {
        LambdaQueryWrapperX<DsProduct> wrapper = new LambdaQueryWrapperX<DsProduct>();
        wrapper.eq(DsProduct::getShopId, shopId);
        wrapper.eq(DsProduct::getSaleStatus, 1);
        wrapper.likeIfPresent(DsProduct::getProductName, reqVO.getKeyword());
        wrapper.orderByAsc(DsProduct::getSort);
        wrapper.orderByDesc(DsProduct::getId);
        applySort(wrapper, reqVO.getSortType());
        return selectPage(reqVO, wrapper);
    }

    default PageResult<DsProduct> selectAdminPage(DsProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, reqVO.getShopId())
                .eqIfPresent(DsProduct::getCategoryId, reqVO.getCategoryId())
                .likeIfPresent(DsProduct::getProductName, reqVO.getProductName())
                .eqIfPresent(DsProduct::getSaleStatus, reqVO.getSaleStatus())
                .orderByAsc(DsProduct::getSort)
                .orderByDesc(DsProduct::getId));
    }

    private static void applySort(LambdaQueryWrapperX<DsProduct> wrapper, String sortType) {
        if ("PRICE_ASC".equals(sortType)) {
            wrapper.orderByAsc(DsProduct::getPriceAmount);
            return;
        }
        if ("PRICE_DESC".equals(sortType)) {
            wrapper.orderByDesc(DsProduct::getPriceAmount);
            return;
        }
        if ("STOCK_ASC".equals(sortType)) {
            wrapper.orderByAsc(DsProduct::getStock);
            return;
        }
        if ("STOCK_DESC".equals(sortType)) {
            wrapper.orderByDesc(DsProduct::getStock);
            return;
        }
        if ("LATEST".equals(sortType)) {
            wrapper.orderByDesc(DsProduct::getId);
        }
    }
}
