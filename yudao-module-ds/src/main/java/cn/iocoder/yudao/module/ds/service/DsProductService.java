package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import jakarta.validation.Valid;

public interface DsProductService {

    Long createProduct(Long uid, @Valid AppDsProductSaveReqVO reqVO);

    void updateProduct(Long uid, @Valid AppDsProductSaveReqVO reqVO);

    void updateProductStatus(Long uid, Long productId, Integer saleStatus);

    PageResult<DsProduct> getMyProductPage(Long uid, AppDsMyProductPageReqVO reqVO);

    PageResult<DsProduct> getShelfProductPage(AppDsProductListReqVO reqVO);

    DsProduct getShelfProduct(Long id);

    Long createAdminProduct(@Valid DsProductSaveReqVO reqVO);

    void updateAdminProduct(@Valid DsProductSaveReqVO reqVO);

    void deleteAdminProduct(Long id);

    DsProduct getAdminProduct(Long id);

    PageResult<DsProduct> getAdminProductPage(DsProductPageReqVO reqVO);

    void updateAdminProductStatus(Long id, Integer saleStatus);

    java.util.Map<Integer, Long> getTabsCount(Long shopId);
}
