package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserAddressMapper;
import cn.iocoder.yudao.module.system.api.notify.NotifyMessageSendApi;
import cn.iocoder.yudao.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PAID;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.SHOP_ORDER_PAY;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STATUS_ILLEGAL;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STOCK_NOT_ENOUGH;

@Service
@Validated
public class DsShopOrderServiceImpl implements DsShopOrderService {

    private static final String MESSAGE_TEMPLATE_ORDER_PAID = "sansanshenghuo";
    private static final DateTimeFormatter PAID_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private DsShopOrderMapper dsShopOrderMapper;
    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsUserAddressMapper dsUserAddressMapper;
    @Resource
    private DsPointAccountService dsPointAccountService;
    @Resource
    private DsShareholderPoolService dsShareholderPoolService;
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DsShopOrder createAndPayOrder(Long uid, AppDsShopOrderCreateReqVO reqVO) {
        Map<Long, Integer> buyCountMap = mergeBuyCount(reqVO.getItems());
        List<DsProduct> products = dsProductMapper.selectBatchIds(buyCountMap.keySet());
        if (products.size() != buyCountMap.size()) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        Map<Long, DsProduct> productMap = products.stream()
                .collect(Collectors.toMap(DsProduct::getId, item -> item, (a, b) -> a, LinkedHashMap::new));
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCount = 0;
        StringBuilder summaryBuilder = new StringBuilder();
        Map<Long, ShopNotifyData> shopNotifyDataMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : buyCountMap.entrySet()) {
            DsProduct product = productMap.get(entry.getKey());
            if (product == null) {
                throw exception(PRODUCT_NOT_EXISTS);
            }
            if (!Integer.valueOf(1).equals(product.getSaleStatus())) {
                throw exception(PRODUCT_STATUS_ILLEGAL);
            }
            int buyCount = entry.getValue();
            if (product.getStock() == null || product.getStock() < buyCount) {
                throw exception(PRODUCT_STOCK_NOT_ENOUGH);
            }
            product.setStock(product.getStock() - buyCount);
            product.setSalesCount((product.getSalesCount() == null ? 0 : product.getSalesCount()) + buyCount);
            dsProductMapper.updateById(product);
            BigDecimal lineAmount = product.getPriceAmount().multiply(BigDecimal.valueOf(buyCount));
            totalAmount = totalAmount.add(lineAmount);
            totalCount += buyCount;
            ShopNotifyData shopNotifyData = shopNotifyDataMap.computeIfAbsent(product.getShopId(), ignored -> new ShopNotifyData());
            shopNotifyData.setTotalAmount(shopNotifyData.getTotalAmount().add(lineAmount));
            shopNotifyData.setItemCount(shopNotifyData.getItemCount() + buyCount);
            if (shopNotifyData.getMarketPrice() == null && product.getMarketPrice() != null) {
                shopNotifyData.setMarketPrice(product.getMarketPrice());
            }
            if (product.getProductName() != null) {
                shopNotifyData.getProductNames().add(product.getProductName());
            }
            if (!summaryBuilder.isEmpty()) {
                summaryBuilder.append("；");
            }
            summaryBuilder.append(product.getProductName()).append(" x").append(buyCount);
        }
        BigDecimal payPoints = totalAmount.setScale(2, java.math.RoundingMode.HALF_UP);
        String orderNo = "S" + IdUtil.fastSimpleUUID();
        LocalDateTime paidAt = LocalDateTime.now();
        dsPointAccountService.spendPoints(uid, payPoints, SHOP_ORDER_PAY.getCode(), orderNo, paidAt);
        DsShopOrder order = DsShopOrder.builder()
                .orderNo(orderNo)
                .uid(uid)
                .itemCount(totalCount)
                .totalAmount(payPoints)
                .payPoints(payPoints)
                .payStatus(PAID.getCode())
                .paidAt(paidAt)
                .productSummary(summaryBuilder.toString())
                .build();
        dsShopOrderMapper.insert(order);
        dsShareholderPoolService.recordShopOrderProfitToPool(order);
        sendOrderPaidNotify(uid, order, shopNotifyDataMap);
        return order;
    }

    @Override
    public List<DsShopOrder> getUserOrders(Long uid) {
        return dsShopOrderMapper.selectListByUid(uid);
    }

    private Map<Long, Integer> mergeBuyCount(List<AppDsShopOrderCreateReqVO.Item> items) {
        Map<Long, Integer> result = new LinkedHashMap<>();
        for (AppDsShopOrderCreateReqVO.Item item : items) {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            result.put(productId, result.getOrDefault(productId, 0) + quantity);
        }
        return result;
    }

    private void sendOrderPaidNotify(Long uid, DsShopOrder order, Map<Long, ShopNotifyData> shopNotifyDataMap) {
        if (shopNotifyDataMap.isEmpty()) {
            return;
        }
        DsUserAddress address = dsUserAddressMapper.selectDefaultByUid(uid);
        for (Map.Entry<Long, ShopNotifyData> entry : shopNotifyDataMap.entrySet()) {
            Long shopId = entry.getKey();
            if (shopId == null) {
                continue;
            }
            DsShop shop = dsShopMapper.selectById(shopId);
            if (shop == null || shop.getBackendAdminUserId() == null) {
                continue;
            }
            ShopNotifyData notifyData = entry.getValue();
            Map<String, Object> templateParams = buildTemplateParams(order, notifyData, address);
            NotifySendSingleToUserReqDTO reqDTO = new NotifySendSingleToUserReqDTO();
            reqDTO.setUserId(shop.getBackendAdminUserId());
            reqDTO.setTemplateCode(MESSAGE_TEMPLATE_ORDER_PAID);
            reqDTO.setTemplateParams(templateParams);
            notifyMessageSendApi.sendSingleMessageToAdmin(reqDTO);
        }
    }

    private Map<String, Object> buildTemplateParams(DsShopOrder order, ShopNotifyData notifyData, DsUserAddress address) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("orderNo", order.getOrderNo());
        params.put("paidAt", order.getPaidAt() == null ? null : order.getPaidAt().format(PAID_AT_FORMATTER));
        params.put("productName", resolveProductName(notifyData));
        params.put("itemCount", notifyData.getItemCount());
        params.put("marketPrice", formatMoney(notifyData.getMarketPrice() == null ? BigDecimal.ZERO : notifyData.getMarketPrice()));
        params.put("totalAmount", formatMoney(notifyData.getTotalAmount()));
        params.put("address", buildArea(address));
        params.put("receiverName", address == null ? "" : address.getReceiverName());
        params.put("receiverMobile", address == null ? "" : address.getReceiverMobile());
        return params;
    }

    private String resolveProductName(ShopNotifyData notifyData) {
        List<String> names = notifyData.getProductNames();
        if (names.isEmpty()) {
            return "";
        }
        if (names.size() == 1) {
            return names.get(0);
        }
        return names.get(0) + "等" + names.size() + "款商品";
    }

    private String buildArea(DsUserAddress address) {
        if (address == null) {
            return "";
        }
        StringBuilder areaBuilder = new StringBuilder();
        appendArea(areaBuilder, address.getProvince());
        appendArea(areaBuilder, address.getCity());
        appendArea(areaBuilder, address.getDistrict());
        return areaBuilder.toString();
    }

    private void appendArea(StringBuilder areaBuilder, String areaPart) {
        if (areaPart == null || areaPart.isBlank()) {
            return;
        }
        if (!areaBuilder.isEmpty()) {
            areaBuilder.append(" ");
        }
        areaBuilder.append(areaPart);
    }

    private String formatMoney(BigDecimal value) {
        return value == null ? "0.00" : value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }


    @Getter
    @Setter
    private static class ShopNotifyData {
        private BigDecimal totalAmount = BigDecimal.ZERO;
        private Integer itemCount = 0;
        private BigDecimal marketPrice;
        private final List<String> productNames = new ArrayList<>();
    }
}
