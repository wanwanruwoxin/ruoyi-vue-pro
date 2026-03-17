package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
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
}
