package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductSkuMapper;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_SKU_NOT_EXISTS;

@Service
@Validated
public class DsProductSkuServiceImpl implements DsProductSkuService {

    @Resource
    private DsProductSkuMapper dsProductSkuMapper;
    @Resource
    private DsProductService dsProductService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProductSku(@Valid DsProductSkuSaveReqVO reqVO) {
        dsProductService.getAdminProduct(reqVO.getSpuId());
        DsProductSku sku = BeanUtils.toBean(reqVO, DsProductSku.class);
        if (sku.getSalesCount() == null) {
            sku.setSalesCount(0);
        }
        if (sku.getFirstBrokeragePrice() == null) {
            sku.setFirstBrokeragePrice(0);
        }
        if (sku.getSecondBrokeragePrice() == null) {
            sku.setSecondBrokeragePrice(0);
        }
        dsProductSkuMapper.insert(sku);
        return sku.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductSku(@Valid DsProductSkuSaveReqVO reqVO) {
        DsProductSku sku = validateProductSku(reqVO.getId());
        dsProductService.getAdminProduct(reqVO.getSpuId());
        BeanUtils.copyProperties(reqVO, sku);
        dsProductSkuMapper.updateById(sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductSku(Long id) {
        validateProductSku(id);
        dsProductSkuMapper.deleteById(id);
    }

    @Override
    public DsProductSku getProductSku(Long id) {
        return validateProductSku(id);
    }

    @Override
    public PageResult<DsProductSku> getProductSkuPage(DsProductSkuPageReqVO reqVO) {
        return dsProductSkuMapper.selectPage(reqVO);
    }

    @Override
    public List<DsProductSku> getProductSkuListBySpuId(Long spuId) {
        return dsProductSkuMapper.selectList(DsProductSku::getSpuId, spuId);
    }

    private DsProductSku validateProductSku(Long id) {
        DsProductSku sku = dsProductSkuMapper.selectById(id);
        if (sku == null) {
            throw exception(PRODUCT_SKU_NOT_EXISTS);
        }
        return sku;
    }
}
