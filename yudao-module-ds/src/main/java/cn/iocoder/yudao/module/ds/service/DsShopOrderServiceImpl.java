package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsOrderCommissionSplit;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductRecommendTrace;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRecommendRewardRecord;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.dal.mysql.DsOrderCommissionSplitMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductRecommendTraceMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsRecommendRewardRecordMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderItemMapper;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PayStatus.PAID;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.SHOP_ORDER_PAY;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.SHOP_RECOMMEND_REWARD;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STATUS_ILLEGAL;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.PRODUCT_STOCK_NOT_ENOUGH;

@Service
@Validated
public class DsShopOrderServiceImpl implements DsShopOrderService {

    private static final String MESSAGE_TEMPLATE_ORDER_PAID = "sansanshenghuo";
    private static final DateTimeFormatter PAID_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String COMMISSION_SETTLE_STATUS_PENDING = "PENDING";
    private static final String REWARD_GRANT_STATUS_PENDING = "PENDING";
    private static final String REWARD_GRANT_STATUS_GRANTED = "GRANTED";
    private static final String SHOP_RECOMMEND_REWARD_RULE_VERSION = "SHOP_COMMISSION_RECOMMEND_V1";
    private static final String RECOMMEND_SCENE_PRODUCT_RECOMMENDER = "PRODUCT_RECOMMENDER";
    private static final int RECOMMEND_STATUS_BOUND = 1;

    @Resource
    private DsShopOrderMapper dsShopOrderMapper;
    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsUserAddressMapper dsUserAddressMapper;
    @Resource
    private DsShopOrderItemMapper dsShopOrderItemMapper;
    @Resource
    private DsOrderCommissionSplitMapper dsOrderCommissionSplitMapper;
    @Resource
    private DsRecommendRewardRecordMapper dsRecommendRewardRecordMapper;
    @Resource
    private DsProductRecommendTraceMapper dsProductRecommendTraceMapper;
    @Resource
    private DsPointAccountService dsPointAccountService;
    @Resource
    private DsRewardRuleService dsRewardRuleService;
    @Resource
    private DsPlatformAccountService dsPlatformAccountService;
    @Resource
    private DsShareholderPoolService dsShareholderPoolService;
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DsShopOrder createAndPayOrder(Long uid, AppDsShopOrderCreateReqVO reqVO) {
        Map<Long, ItemPurchaseData> purchaseDataMap = mergeBuyCount(reqVO.getItems());
        List<DsProduct> products = dsProductMapper.selectByIds(purchaseDataMap.keySet());
        if (products.size() != purchaseDataMap.size()) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        Map<Long, DsProduct> productMap = products.stream()
                .collect(Collectors.toMap(DsProduct::getId, item -> item, (a, b) -> a, LinkedHashMap::new));
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalCount = 0;
        StringBuilder summaryBuilder = new StringBuilder();
        Map<Long, ShopNotifyData> shopNotifyDataMap = new LinkedHashMap<>();
        List<ItemSettlementData> itemSettlementDataList = new ArrayList<>();
        for (Map.Entry<Long, ItemPurchaseData> entry : purchaseDataMap.entrySet()) {
            DsProduct product = productMap.get(entry.getKey());
            if (product == null) {
                throw exception(PRODUCT_NOT_EXISTS);
            }
            if (!Integer.valueOf(1).equals(product.getSaleStatus())) {
                throw exception(PRODUCT_STATUS_ILLEGAL);
            }
            ItemPurchaseData purchaseData = entry.getValue();
            int buyCount = purchaseData.buyCount();
            if (product.getStock() == null || product.getStock() < buyCount) {
                throw exception(PRODUCT_STOCK_NOT_ENOUGH);
            }
            product.setStock(product.getStock() - buyCount);
            product.setSalesCount((product.getSalesCount() == null ? 0 : product.getSalesCount()) + buyCount);
            dsProductMapper.updateById(product);
            BigDecimal lineAmount = product.getPriceAmount().multiply(BigDecimal.valueOf(buyCount));
            totalAmount = totalAmount.add(lineAmount);
            totalCount += buyCount;
            itemSettlementDataList.add(new ItemSettlementData(product, buyCount, lineAmount,
                    resolveProductRecommenderUid(uid, purchaseData.recommenderUid())));
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
        persistOrderItemsAndReward(uid, order, paidAt, itemSettlementDataList);
        dsShareholderPoolService.recordShopOrderProfitToPool(order);
        sendOrderPaidNotify(uid, order, shopNotifyDataMap);
        return order;
    }

    @Override
    public List<DsShopOrder> getUserOrders(Long uid) {
        return dsShopOrderMapper.selectListByUid(uid);
    }

    private Map<Long, ItemPurchaseData> mergeBuyCount(List<AppDsShopOrderCreateReqVO.Item> items) {
        Map<Long, ItemPurchaseData> result = new LinkedHashMap<>();
        for (AppDsShopOrderCreateReqVO.Item item : items) {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();
            ItemPurchaseData current = result.get(productId);
            if (current == null) {
                result.put(productId, new ItemPurchaseData(quantity, item.getRecommenderUid()));
                continue;
            }
            Long recommenderUid = current.recommenderUid();
            if (recommenderUid == null && item.getRecommenderUid() != null) {
                recommenderUid = item.getRecommenderUid();
            }
            result.put(productId, new ItemPurchaseData(current.buyCount() + quantity, recommenderUid));
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

    private void persistOrderItemsAndReward(Long uid, DsShopOrder order, LocalDateTime paidAt, List<ItemSettlementData> items) {
        DsRewardRuleService.ShopOrderCommissionRuleConfig commissionRuleConfig = dsRewardRuleService.getShopOrderCommissionRuleConfig();
        BigDecimal platformRate = commissionRuleConfig.platformRate();
        BigDecimal rewardRate = commissionRuleConfig.recommendRewardRate();
        BigDecimal rewardShareRate = calculateRewardShareRate(platformRate, rewardRate);
        RewardPersistData persistData = new RewardPersistData(items.size());
        for (ItemSettlementData item : items) {
            Long inviterId = item.recommenderUid();
            Long orderItemId = IdUtil.getSnowflakeNextId();
            DsShopOrderItem orderItem = buildOrderItem(uid, order, paidAt, item, orderItemId);
            persistData.orderItems().add(orderItem);
            String traceNo = buildRecommendTrace(uid, order, paidAt, inviterId, orderItemId, orderItem, persistData.recommendTraces());
            CommissionAmounts commissionAmounts = calculateCommissionAmounts(orderItem.getLineAmount(), platformRate, rewardRate, inviterId);
            Long splitId = IdUtil.getSnowflakeNextId();
            persistData.commissionSplits().add(buildCommissionSplit(order, paidAt, platformRate, rewardShareRate, splitId, orderItem,
                    commissionAmounts));
            DsRecommendRewardRecord rewardRecord = buildRewardRecord(uid, order, paidAt, inviterId, splitId, orderItemId, traceNo,
                    rewardRate, commissionAmounts.recommendRewardAmount());
            if (rewardRecord != null) {
                persistData.rewardRecords().add(rewardRecord);
                persistData.inviterRewardAmountMap().merge(inviterId, rewardRecord.getRewardAmount(), BigDecimal::add);
            }
        }
        persistBatchAndGrantReward(uid, order, paidAt, commissionRuleConfig, persistData);
    }

    private BigDecimal calculateRewardShareRate(BigDecimal platformRate, BigDecimal rewardRate) {
        return platformRate.signum() <= 0 ? BigDecimal.ZERO : rewardRate.divide(platformRate, 4, RoundingMode.HALF_UP);
    }

    private DsShopOrderItem buildOrderItem(Long uid, DsShopOrder order, LocalDateTime paidAt, ItemSettlementData item, Long orderItemId) {
        return DsShopOrderItem.builder()
                .id(orderItemId)
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .uid(uid)
                .shopId(item.product().getShopId())
                .productId(item.product().getId())
                .productName(item.product().getProductName() == null ? "" : item.product().getProductName())
                .priceAmount(item.product().getPriceAmount())
                .quantity(item.buyCount())
                .lineAmount(item.lineAmount())
                .paidAt(paidAt)
                .build();
    }

    private String buildRecommendTrace(Long uid, DsShopOrder order, LocalDateTime paidAt, Long inviterId, Long orderItemId,
                                       DsShopOrderItem orderItem, List<DsProductRecommendTrace> recommendTraces) {
        if (inviterId == null) {
            return null;
        }
        String traceNo = "SPT" + IdUtil.fastSimpleUUID();
        recommendTraces.add(DsProductRecommendTrace.builder()
                .id(IdUtil.getSnowflakeNextId())
                .traceNo(traceNo)
                .inviterId(inviterId)
                .inviteeId(uid)
                .shopId(orderItem.getShopId())
                .productId(orderItem.getProductId())
                .recommendScene(RECOMMEND_SCENE_PRODUCT_RECOMMENDER)
                .recommendStatus(RECOMMEND_STATUS_BOUND)
                .recommendedAt(paidAt)
                .bindOrderNo(order.getOrderNo())
                .bindOrderItemId(orderItemId)
                .build());
        return traceNo;
    }

    private CommissionAmounts calculateCommissionAmounts(BigDecimal lineAmount, BigDecimal platformRate, BigDecimal rewardRate,
                                                         Long inviterId) {
        BigDecimal grossAmount = lineAmount == null ? BigDecimal.ZERO : lineAmount;
        BigDecimal platformAmount = grossAmount.multiply(platformRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal recommendRewardAmount = grossAmount.multiply(rewardRate).setScale(2, RoundingMode.HALF_UP);
        if (inviterId == null) {
            recommendRewardAmount = BigDecimal.ZERO;
        } else if (recommendRewardAmount.compareTo(platformAmount) > 0) {
            recommendRewardAmount = platformAmount;
        }
        BigDecimal platformNetAmount = platformAmount.subtract(recommendRewardAmount).setScale(2, RoundingMode.HALF_UP);
        return new CommissionAmounts(grossAmount, platformAmount, recommendRewardAmount, platformNetAmount);
    }

    private DsOrderCommissionSplit buildCommissionSplit(DsShopOrder order, LocalDateTime paidAt, BigDecimal platformRate,
                                                        BigDecimal rewardShareRate, Long splitId, DsShopOrderItem orderItem,
                                                        CommissionAmounts amounts) {
        return DsOrderCommissionSplit.builder()
                .id(splitId)
                .splitNo("SCS" + IdUtil.fastSimpleUUID())
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .orderItemId(orderItem.getId())
                .shopId(orderItem.getShopId())
                .grossAmount(amounts.grossAmount())
                .platformRate(platformRate)
                .platformAmount(amounts.platformAmount())
                .rewardShareRate(rewardShareRate)
                .rewardAmount(amounts.recommendRewardAmount())
                .platformNetAmount(amounts.platformNetAmount())
                .settleStatus(COMMISSION_SETTLE_STATUS_PENDING)
                .occurredAt(paidAt)
                .build();
    }

    private DsRecommendRewardRecord buildRewardRecord(Long uid, DsShopOrder order, LocalDateTime paidAt, Long inviterId, Long splitId,
                                                      Long orderItemId, String traceNo, BigDecimal rewardRate,
                                                      BigDecimal recommendRewardAmount) {
        if (inviterId == null || recommendRewardAmount.signum() <= 0) {
            return null;
        }
        return DsRecommendRewardRecord.builder()
                .id(IdUtil.getSnowflakeNextId())
                .rewardNo("SRR" + IdUtil.fastSimpleUUID())
                .splitId(splitId)
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .orderItemId(orderItemId)
                .traceNo(traceNo)
                .inviterId(inviterId)
                .inviteeId(uid)
                .rewardRate(rewardRate)
                .rewardAmount(recommendRewardAmount)
                .grantStatus(REWARD_GRANT_STATUS_PENDING)
                .occurredAt(paidAt)
                .build();
    }

    private void persistBatchAndGrantReward(Long uid, DsShopOrder order, LocalDateTime paidAt,
                                            DsRewardRuleService.ShopOrderCommissionRuleConfig commissionRuleConfig,
                                            RewardPersistData persistData) {
        if (!persistData.orderItems().isEmpty()) {
            dsShopOrderItemMapper.insertBatch(persistData.orderItems());
        }
        if (!persistData.recommendTraces().isEmpty()) {
            dsProductRecommendTraceMapper.insertBatch(persistData.recommendTraces());
        }
        if (!persistData.commissionSplits().isEmpty()) {
            dsOrderCommissionSplitMapper.insertBatch(persistData.commissionSplits());
            dsPlatformAccountService.recordCommissionIncome(order.getId(), order.getOrderNo(), paidAt, persistData.commissionSplits());
        }
        if (persistData.rewardRecords().isEmpty()) {
            return;
        }
        dsRecommendRewardRecordMapper.insertBatch(persistData.rewardRecords());
        String rewardRuleVersion = commissionRuleConfig.recommendRuleVersion() == null
                ? SHOP_RECOMMEND_REWARD_RULE_VERSION
                : commissionRuleConfig.recommendRuleVersion();
        persistData.inviterRewardAmountMap().forEach((inviterId, rewardAmount) ->
                dsPointAccountService.earnPoints(inviterId, rewardAmount, SHOP_RECOMMEND_REWARD.getCode(),
                        "SRB" + order.getOrderNo() + "-" + inviterId, uid, rewardRuleVersion, paidAt));
        persistData.rewardRecords().forEach(reward -> {
            reward.setGrantStatus(REWARD_GRANT_STATUS_GRANTED);
            reward.setGrantedAt(paidAt);
        });
        dsRecommendRewardRecordMapper.updateBatch(persistData.rewardRecords());
    }

    private Long resolveProductRecommenderUid(Long uid, Long recommenderUid) {
        if (recommenderUid == null || recommenderUid <= 0 || Objects.equals(uid, recommenderUid)) {
            return null;
        }
        return recommenderUid;
    }

    private record CommissionAmounts(BigDecimal grossAmount, BigDecimal platformAmount, BigDecimal recommendRewardAmount,
                                     BigDecimal platformNetAmount) {
    }

    private record RewardPersistData(List<DsShopOrderItem> orderItems, List<DsProductRecommendTrace> recommendTraces,
                                     List<DsOrderCommissionSplit> commissionSplits, List<DsRecommendRewardRecord> rewardRecords,
                                     Map<Long, BigDecimal> inviterRewardAmountMap) {
        private RewardPersistData(int itemSize) {
            this(new ArrayList<>(itemSize), new ArrayList<>(itemSize), new ArrayList<>(itemSize), new ArrayList<>(itemSize),
                    new LinkedHashMap<>());
        }
    }

    private record ItemSettlementData(DsProduct product, int buyCount, BigDecimal lineAmount, Long recommenderUid) {
    }

    private record ItemPurchaseData(int buyCount, Long recommenderUid) {
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
