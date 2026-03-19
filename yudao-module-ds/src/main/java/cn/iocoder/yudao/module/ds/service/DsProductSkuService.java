package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import jakarta.validation.Valid;

import java.util.List;

public interface DsProductSkuService {

    Long createProductSku(@Valid DsProductSkuSaveReqVO reqVO);

    void updateProductSku(@Valid DsProductSkuSaveReqVO reqVO);

    void deleteProductSku(Long id);

    DsProductSku getProductSku(Long id);

    PageResult<DsProductSku> getProductSkuPage(DsProductSkuPageReqVO reqVO);

    List<DsProductSku> getProductSkuListBySpuId(Long spuId);
}
