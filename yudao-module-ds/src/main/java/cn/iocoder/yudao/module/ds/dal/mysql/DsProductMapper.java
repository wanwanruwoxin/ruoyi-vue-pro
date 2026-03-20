package cn.iocoder.yudao.module.ds.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.hutool.core.util.ObjectUtil;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DsProductMapper extends BaseMapperX<DsProduct> {

    int ALERT_STOCK = 10;

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
        LambdaQueryWrapperX<DsProduct> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(DsProduct::getShopId, reqVO.getShopId());
        queryWrapper.eqIfPresent(DsProduct::getCategoryId, reqVO.getCategoryId());
        queryWrapper.likeIfPresent(DsProduct::getProductName, reqVO.getProductName());
        queryWrapper.eqIfPresent(DsProduct::getSaleStatus, reqVO.getSaleStatus());
        queryWrapper.betweenIfPresent(DsProduct::getCreateTime, reqVO.getCreateTime());
        queryWrapper.orderByAsc(DsProduct::getSort);
        queryWrapper.orderByDesc(DsProduct::getId);
        appendTabQuery(reqVO.getTabType(), queryWrapper);
        return selectPage(reqVO, queryWrapper);
    }

    default Long selectAlertStockCount(Long shopId) {
        return selectCount(new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, shopId)
                .le(DsProduct::getStock, ALERT_STOCK)
                .ne(DsProduct::getSaleStatus, -1));
    }

    default Long selectForSaleCount(Long shopId) {
        return selectCount(new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, shopId)
                .eq(DsProduct::getSaleStatus, 1));
    }

    default Long selectInWarehouseCount(Long shopId) {
        return selectCount(new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, shopId)
                .eq(DsProduct::getSaleStatus, 0));
    }

    default Long selectSoldOutCount(Long shopId) {
        return selectCount(new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, shopId)
                .eq(DsProduct::getStock, 0)
                .ne(DsProduct::getSaleStatus, -1));
    }

    default Long selectRecycleCount(Long shopId) {
        return selectCount(new LambdaQueryWrapperX<DsProduct>()
                .eqIfPresent(DsProduct::getShopId, shopId)
                .eq(DsProduct::getSaleStatus, -1));
    }

    private static void appendTabQuery(Integer tabType, LambdaQueryWrapperX<DsProduct> queryWrapper) {
        if (ObjectUtil.equals(DsProductPageReqVO.FOR_SALE, tabType)) {
            queryWrapper.eq(DsProduct::getSaleStatus, 1);
        }
        if (ObjectUtil.equals(DsProductPageReqVO.IN_WAREHOUSE, tabType)) {
            queryWrapper.eq(DsProduct::getSaleStatus, 0);
        }
        if (ObjectUtil.equals(DsProductPageReqVO.SOLD_OUT, tabType)) {
            queryWrapper.eq(DsProduct::getStock, 0).ne(DsProduct::getSaleStatus, -1);
        }
        if (ObjectUtil.equals(DsProductPageReqVO.ALERT_STOCK, tabType)) {
            queryWrapper.le(DsProduct::getStock, ALERT_STOCK).ne(DsProduct::getSaleStatus, -1);
        }
        if (ObjectUtil.equals(DsProductPageReqVO.RECYCLE_BIN, tabType)) {
            queryWrapper.eq(DsProduct::getSaleStatus, -1);
        }
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
