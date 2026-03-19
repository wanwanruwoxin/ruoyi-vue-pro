package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import jakarta.validation.Valid;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_ACCESS_DENIED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STATUS_ILLEGAL;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;

@Service
@Validated
public class DsProductServiceImpl implements DsProductService {

    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsShopService dsShopService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(Long uid, @Valid AppDsProductSaveReqVO reqVO) {
        DsShop shop = getUserShop(uid);
        DsProduct product = new DsProduct();
        fillProductFields(product, reqVO);
        product.setShopId(shop.getId());
        product.setSpecType(false);
        product.setSalesCount(0);
        product.setVirtualSalesCount(0);
        product.setBrowseCount(0);
        product.setSaleStatus(0);
        dsProductMapper.insert(product);
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Long uid, @Valid AppDsProductSaveReqVO reqVO) {
        DsProduct product = validateOwnerProduct(uid, reqVO.getId());
        fillProductFields(product, reqVO);
        dsProductMapper.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStatus(Long uid, Long productId, Integer saleStatus) {
        if (!Integer.valueOf(0).equals(saleStatus) && !Integer.valueOf(1).equals(saleStatus)) {
            throw exception(PRODUCT_STATUS_ILLEGAL);
        }
        DsProduct product = validateOwnerProduct(uid, productId);
        product.setSaleStatus(saleStatus);
        dsProductMapper.updateById(product);
    }

    @Override
    public PageResult<DsProduct> getMyProductPage(Long uid, AppDsMyProductPageReqVO reqVO) {
        DsShop shop = getUserShop(uid);
        return dsProductMapper.selectMyPage(shop.getId(), reqVO);
    }

    @Override
    public PageResult<DsProduct> getShelfProductPage(AppDsProductListReqVO reqVO) {
        dsShopService.validateShopById(reqVO.getShopId());
        return dsProductMapper.selectShelfPage(reqVO.getShopId(), reqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAdminProduct(@Valid DsProductSaveReqVO reqVO) {
        dsShopService.validateShopById(reqVO.getShopId());
        DsProduct product = new DsProduct();
        fillProductFields(product, reqVO);
        dsProductMapper.insert(product);
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminProduct(@Valid DsProductSaveReqVO reqVO) {
        DsProduct product = validateAdminProduct(reqVO.getId());
        dsShopService.validateShopById(reqVO.getShopId());
        fillProductFields(product, reqVO);
        dsProductMapper.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdminProduct(Long id) {
        validateAdminProduct(id);
        dsProductMapper.deleteById(id);
    }

    @Override
    public DsProduct getAdminProduct(Long id) {
        return validateAdminProduct(id);
    }

    @Override
    public PageResult<DsProduct> getAdminProductPage(DsProductPageReqVO reqVO) {
        return dsProductMapper.selectAdminPage(reqVO);
    }

    private DsProduct validateOwnerProduct(Long uid, Long productId) {
        DsProduct product = dsProductMapper.selectById(productId);
        if (product == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        DsShop shop = getUserShop(uid);
        if (!shop.getId().equals(product.getShopId())) {
            throw exception(PRODUCT_ACCESS_DENIED);
        }
        return product;
    }

    private DsShop getUserShop(Long uid) {
        DsShop shop = dsShopService.getShopByUid(uid);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        return shop;
    }

    private DsProduct validateAdminProduct(Long id) {
        DsProduct product = dsProductMapper.selectById(id);
        if (product == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        return product;
    }

    private static void fillProductFields(DsProduct product, AppDsProductSaveReqVO reqVO) {
        product.setProductName(reqVO.getProductName());
        product.setPicUrl(firstUrl(reqVO.getImageUrls()));
        product.setSliderPicUrls(joinUrls(reqVO.getImageUrls()));
        product.setPriceAmount(reqVO.getPriceAmount());
        product.setStock(reqVO.getStock());
        product.setDetailDesc(reqVO.getDetailDesc());
        product.setSort(reqVO.getSort());
        product.setSpecType(false);
        product.setImageUrls(joinUrls(reqVO.getImageUrls()));
        product.setVideoUrls(joinUrls(reqVO.getVideoUrls()));
    }

    private static void fillProductFields(DsProduct product, DsProductSaveReqVO reqVO) {
        product.setShopId(reqVO.getShopId());
        product.setCategoryId(reqVO.getCategoryId());
        product.setProductName(reqVO.getProductName());
        product.setKeyword(reqVO.getKeyword());
        product.setIntroduction(reqVO.getIntroduction());
        product.setPicUrl(reqVO.getPicUrl());
        product.setSliderPicUrls(reqVO.getSliderPicUrls());
        product.setPriceAmount(reqVO.getPriceAmount());
        product.setMarketPrice(reqVO.getMarketPrice());
        product.setCostPrice(reqVO.getCostPrice());
        product.setStock(reqVO.getStock());
        product.setDetailDesc(reqVO.getDetailDesc());
        product.setSort(reqVO.getSort());
        product.setSpecType(reqVO.getSpecType());
        product.setGiveIntegral(reqVO.getGiveIntegral());
        product.setSaleStatus(reqVO.getSaleStatus());
        product.setImageUrls(reqVO.getImageUrls());
        product.setVideoUrls(reqVO.getVideoUrls());
        if (product.getSalesCount() == null) {
            product.setSalesCount(0);
        }
        if (product.getVirtualSalesCount() == null) {
            product.setVirtualSalesCount(0);
        }
        if (product.getBrowseCount() == null) {
            product.setBrowseCount(0);
        }
    }

    private static String firstUrl(List<String> urls) {
        if (CollUtil.isEmpty(urls)) {
            return null;
        }
        return urls.stream().filter(StrUtil::isNotBlank).findFirst().orElse(null);
    }

    private static String joinUrls(List<String> urls) {
        if (CollUtil.isEmpty(urls)) {
            return null;
        }
        List<String> filteredUrls = urls.stream().filter(StrUtil::isNotBlank).toList();
        if (CollUtil.isEmpty(filteredUrls)) {
            return null;
        }
        return StrUtil.join(",", filteredUrls);
    }
}
