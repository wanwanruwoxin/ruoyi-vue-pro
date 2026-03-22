package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Resource
    private DsShopOrderMapper dsShopOrderMapper;
    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsPointAccountService dsPointAccountService;
    @Resource
    private DsShareholderPoolService dsShareholderPoolService;

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
            if (summaryBuilder.length() > 0) {
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
}
