package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductSkuMapper;
import cn.iocoder.yudao.module.ds.enums.DsShopAuditStatusEnum;
import jakarta.validation.Valid;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_ACCESS_DENIED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STATUS_ILLEGAL;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_APPROVED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;

@Service
@Validated
public class DsProductServiceImpl implements DsProductService {

    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsProductSkuMapper dsProductSkuMapper;
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
    public DsProduct getShelfProduct(Long id) {
        DsProduct product = dsProductMapper.selectById(id);
        if (product == null || !Integer.valueOf(1).equals(product.getSaleStatus())) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        dsShopService.validateShopById(product.getShopId());
        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAdminProduct(@Valid DsProductSaveReqVO reqVO) {
        dsShopService.validateShopById(reqVO.getShopId());
        DsProduct product = new DsProduct();
        fillProductFields(product, reqVO);
        List<DsProductSku> skuList = buildSkuListFromReq(reqVO, product);
        initProductFromSkus(product, skuList);
        dsProductMapper.insert(product);
        syncProductSkus(product.getId(), skuList);
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminProduct(@Valid DsProductSaveReqVO reqVO) {
        DsProduct product = validateAdminProduct(reqVO.getId());
        dsShopService.validateShopById(reqVO.getShopId());
        fillProductFields(product, reqVO);
        List<DsProductSku> skuList = buildSkuListFromReq(reqVO, product);
        initProductFromSkus(product, skuList);
        dsProductMapper.updateById(product);
        syncProductSkus(product.getId(), skuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdminProduct(Long id) {
        validateAdminProduct(id);
        dsProductSkuMapper.delete(DsProductSku::getSpuId, id);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminProductStatus(Long id, Integer saleStatus) {
        if (!Integer.valueOf(-1).equals(saleStatus)
                && !Integer.valueOf(0).equals(saleStatus)
                && !Integer.valueOf(1).equals(saleStatus)) {
            throw exception(PRODUCT_STATUS_ILLEGAL);
        }
        DsProduct product = validateAdminProduct(id);
        product.setSaleStatus(saleStatus);
        dsProductMapper.updateById(product);
    }

    @Override
    public Map<Integer, Long> getTabsCount(Long shopId) {
        Map<Integer, Long> tabsCount = new HashMap<>();
        tabsCount.put(DsProductPageReqVO.FOR_SALE, dsProductMapper.selectForSaleCount(shopId));
        tabsCount.put(DsProductPageReqVO.IN_WAREHOUSE, dsProductMapper.selectInWarehouseCount(shopId));
        tabsCount.put(DsProductPageReqVO.SOLD_OUT, dsProductMapper.selectSoldOutCount(shopId));
        tabsCount.put(DsProductPageReqVO.ALERT_STOCK, dsProductMapper.selectAlertStockCount(shopId));
        tabsCount.put(DsProductPageReqVO.RECYCLE_BIN, dsProductMapper.selectRecycleCount(shopId));
        return tabsCount;
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
        if (!DsShopAuditStatusEnum.APPROVED.getStatus().equals(shop.getStatus())) {
            throw exception(SHOP_NOT_APPROVED);
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
        product.setDescription(reqVO.getDetailDesc());
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
        product.setDescription(reqVO.getDescription());
        product.setPicUrl(reqVO.getPicUrl());
        product.setSliderPicUrls(reqVO.getSliderPicUrls());
        product.setPriceAmount(reqVO.getPriceAmount());
        product.setMarketPrice(reqVO.getMarketPrice());
        product.setCostPrice(reqVO.getCostPrice());
        product.setStock(reqVO.getStock());
        product.setDetailDesc(reqVO.getDetailDesc());
        product.setSort(reqVO.getSort());
        product.setSpecType(reqVO.getSpecType());
        product.setBrandId(reqVO.getBrandId());
        product.setDeliveryTypes(reqVO.getDeliveryTypes());
        product.setDeliveryTemplateId(reqVO.getDeliveryTemplateId());
        product.setGiveIntegral(reqVO.getGiveIntegral());
        product.setSubCommissionType(reqVO.getSubCommissionType());
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

    private List<DsProductSku> buildSkuListFromReq(DsProductSaveReqVO reqVO, DsProduct product) {
        if (CollUtil.isEmpty(reqVO.getSkus())) {
            return List.of(buildDefaultSku(product));
        }
        return reqVO.getSkus().stream().map(item -> {
            DsProductSku sku = new DsProductSku();
            sku.setName(StrUtil.blankToDefault(item.getName(), product.getProductName()));
            sku.setPropertiesJson(item.getPropertiesJson());
            sku.setPriceAmount(item.getPriceAmount());
            sku.setMarketPrice(item.getMarketPrice());
            sku.setCostPrice(item.getCostPrice());
            sku.setBarCode(item.getBarCode());
            sku.setPicUrl(item.getPicUrl());
            sku.setStock(item.getStock());
            sku.setWeight(item.getWeight());
            sku.setVolume(item.getVolume());
            sku.setFirstBrokeragePrice(Objects.requireNonNullElse(item.getFirstBrokeragePrice(), 0));
            sku.setSecondBrokeragePrice(Objects.requireNonNullElse(item.getSecondBrokeragePrice(), 0));
            sku.setSalesCount(Objects.requireNonNullElse(item.getSalesCount(), 0));
            sku.setSaleTime(item.getSaleTime());
            return sku;
        }).toList();
    }

    private DsProductSku buildDefaultSku(DsProduct product) {
        DsProductSku sku = new DsProductSku();
        sku.setName(product.getProductName());
        sku.setPriceAmount(product.getPriceAmount());
        sku.setMarketPrice(product.getMarketPrice());
        sku.setCostPrice(product.getCostPrice());
        sku.setPicUrl(product.getPicUrl());
        sku.setStock(product.getStock());
        sku.setFirstBrokeragePrice(0);
        sku.setSecondBrokeragePrice(0);
        sku.setSalesCount(0);
        return sku;
    }

    private void initProductFromSkus(DsProduct product, List<DsProductSku> skus) {
        product.setPriceAmount(skus.stream()
                .map(DsProductSku::getPriceAmount)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(product.getPriceAmount()));
        product.setMarketPrice(skus.stream()
                .map(DsProductSku::getMarketPrice)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(product.getMarketPrice()));
        product.setCostPrice(skus.stream()
                .map(DsProductSku::getCostPrice)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(product.getCostPrice()));
        int stock = skus.stream()
                .map(DsProductSku::getStock)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        product.setStock(stock);
        int salesCount = skus.stream()
                .map(DsProductSku::getSalesCount)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        product.setSalesCount(salesCount);
        if (product.getSpecType() == null) {
            product.setSpecType(skus.size() > 1);
        }
        if (product.getPriceAmount() == null) {
            product.setPriceAmount(BigDecimal.valueOf(0.01D));
        }
    }

    private void syncProductSkus(Long spuId, List<DsProductSku> skus) {
        dsProductSkuMapper.delete(DsProductSku::getSpuId, spuId);
        skus.forEach(sku -> {
            sku.setId(null);
            sku.setSpuId(spuId);
            dsProductSkuMapper.insert(sku);
        });
    }
}
