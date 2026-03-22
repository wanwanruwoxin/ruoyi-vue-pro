package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShareholderPoolRecord;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShareholderPoolRecordMapper;
import cn.iocoder.yudao.module.ds.enums.DsTeamConfigConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.SHAREHOLDER_POOL_DIVIDEND;

@Service
@Validated
public class DsShareholderPoolServiceImpl implements DsShareholderPoolService {

    private static final String SOURCE_TYPE_MEMBERSHIP_ORDER_PROFIT = "MEMBERSHIP_ORDER_PROFIT";
    private static final String SOURCE_TYPE_SHOP_ORDER_PROFIT = "SHOP_ORDER_PROFIT";
    private static final String SETTLE_STATUS_PENDING = "PENDING";
    private static final String SETTLE_STATUS_SETTLED = "SETTLED";
    private static final String DIVIDEND_RULE_VERSION = "SHAREHOLDER_POOL_MONTHLY_V1";

    @Resource
    private DsShareholderPoolRecordMapper dsShareholderPoolRecordMapper;
    @Resource
    private DsMembershipAccountService dsMembershipAccountService;
    @Resource
    private DsPointAccountService dsPointAccountService;
    @Resource
    private DsTeamConfigService dsTeamConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordMembershipOrderProfitToPool(DsMembershipOrder order) {
        if (order == null || order.getPayableAmount() == null || order.getPayableAmount().signum() <= 0) {
            return;
        }
        BigDecimal poolRate = resolvePoolRate();
        if (poolRate.signum() <= 0) {
            return;
        }
        BigDecimal poolAmount = order.getPayableAmount().multiply(poolRate).setScale(2, RoundingMode.HALF_UP);
        if (poolAmount.signum() <= 0) {
            return;
        }
        LocalDateTime occurredAt = order.getPaidAt() != null ? order.getPaidAt() : LocalDateTime.now();
        DsShareholderPoolRecord record = DsShareholderPoolRecord.builder()
                .bizNo("SPR" + IdUtil.fastSimpleUUID())
                .sourceType(SOURCE_TYPE_MEMBERSHIP_ORDER_PROFIT)
                .sourceOrderNo(order.getOrderNo())
                .sourceUid(order.getUid())
                .profitAmount(order.getPayableAmount())
                .poolRate(poolRate)
                .poolAmount(poolAmount)
                .settleMonth(resolveSettleMonth(occurredAt))
                .settleStatus(SETTLE_STATUS_PENDING)
                .occurredAt(occurredAt)
                .build();
        dsShareholderPoolRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordShopOrderProfitToPool(DsShopOrder order) {
        if (order == null || order.getTotalAmount() == null || order.getTotalAmount().signum() <= 0) {
            return;
        }
        BigDecimal poolRate = resolvePoolRate();
        if (poolRate.signum() <= 0) {
            return;
        }
        BigDecimal poolAmount = order.getTotalAmount().multiply(poolRate).setScale(2, RoundingMode.HALF_UP);
        if (poolAmount.signum() <= 0) {
            return;
        }
        LocalDateTime occurredAt = order.getPaidAt() != null ? order.getPaidAt() : LocalDateTime.now();
        DsShareholderPoolRecord record = DsShareholderPoolRecord.builder()
                .bizNo("SPR" + IdUtil.fastSimpleUUID())
                .sourceType(SOURCE_TYPE_SHOP_ORDER_PROFIT)
                .sourceOrderNo(order.getOrderNo())
                .sourceUid(order.getUid())
                .profitAmount(order.getTotalAmount())
                .poolRate(poolRate)
                .poolAmount(poolAmount)
                .settleMonth(resolveSettleMonth(occurredAt))
                .settleStatus(SETTLE_STATUS_PENDING)
                .occurredAt(occurredAt)
                .build();
        dsShareholderPoolRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleMonthlyDividend(YearMonth settleMonth) {
        String month = settleMonth.toString();
        List<DsShareholderPoolRecord> records = dsShareholderPoolRecordMapper
                .selectListBySettleMonthAndStatus(month, SETTLE_STATUS_PENDING);
        if (records.isEmpty()) {
            return;
        }
        List<Long> shareholderUids = dsMembershipAccountService.listActiveShareholderUids();
        if (shareholderUids.isEmpty()) {
            return;
        }
        BigDecimal totalPoolAmount = records.stream()
                .map(DsShareholderPoolRecord::getPoolAmount)
                .filter(item -> item != null && item.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalPoolAmount.signum() <= 0) {
            markRecordsSettled(records, "SPB" + IdUtil.fastSimpleUUID(), LocalDateTime.now());
            return;
        }
        BigDecimal average = totalPoolAmount.divide(BigDecimal.valueOf(shareholderUids.size()), 2, RoundingMode.DOWN);
        BigDecimal distributed = average.multiply(BigDecimal.valueOf(shareholderUids.size()));
        BigDecimal remainder = totalPoolAmount.subtract(distributed);
        String batchNo = "SPB" + IdUtil.fastSimpleUUID();
        LocalDateTime settledAt = LocalDateTime.now();
        for (int i = 0; i < shareholderUids.size(); i++) {
            Long uid = shareholderUids.get(i);
            BigDecimal dividend = i == shareholderUids.size() - 1 ? average.add(remainder) : average;
            if (dividend.signum() <= 0) {
                continue;
            }
            dsPointAccountService.earnPoints(uid, dividend, SHAREHOLDER_POOL_DIVIDEND.getCode(),
                    batchNo + "-" + uid, null, DIVIDEND_RULE_VERSION, settledAt);
        }
        markRecordsSettled(records, batchNo, settledAt);
    }

    private void markRecordsSettled(List<DsShareholderPoolRecord> records, String batchNo, LocalDateTime settledAt) {
        for (DsShareholderPoolRecord record : records) {
            record.setSettleStatus(SETTLE_STATUS_SETTLED);
            record.setSettleBatchNo(batchNo);
            record.setSettledAt(settledAt);
            dsShareholderPoolRecordMapper.updateById(record);
        }
    }

    private BigDecimal resolvePoolRate() {
        Map<String, String> map = dsTeamConfigService.getConfigValueMap(List.of(DsTeamConfigConstants.KEY_SHAREHOLDER_POOL_RATE));
        String raw = map.get(DsTeamConfigConstants.KEY_SHAREHOLDER_POOL_RATE);
        if (raw == null || raw.isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            BigDecimal percent = new BigDecimal(raw.trim());
            if (percent.signum() <= 0) {
                return BigDecimal.ZERO;
            }
            return percent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        } catch (NumberFormatException ignored) {
            return BigDecimal.ZERO;
        }
    }

    private String resolveSettleMonth(LocalDateTime occurredAt) {
        return YearMonth.from(occurredAt).plusMonths(1).toString();
    }
}
