package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsOrderCommissionSplit;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccountLedger;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPlatformAccountLedgerMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPlatformAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class DsPlatformAccountServiceImpl implements DsPlatformAccountService {

    private static final String MAIN_ACCOUNT_CODE = "MAIN";
    private static final String CHANGE_TYPE_INCOME = "INCOME";
    private static final String BIZ_TYPE_SHOP_ORDER_COMMISSION = "SHOP_ORDER_COMMISSION";

    @Resource
    private DsPlatformAccountMapper dsPlatformAccountMapper;
    @Resource
    private DsPlatformAccountLedgerMapper dsPlatformAccountLedgerMapper;

    @Override
    public DsPlatformAccount getOrCreateMainAccount() {
        DsPlatformAccount account = dsPlatformAccountMapper.selectByAccountCode(MAIN_ACCOUNT_CODE);
        if (account != null) {
            return account;
        }
        account = DsPlatformAccount.builder()
                .accountCode(MAIN_ACCOUNT_CODE)
                .availableAmount(BigDecimal.ZERO)
                .frozenAmount(BigDecimal.ZERO)
                .totalIncomeAmount(BigDecimal.ZERO)
                .totalExpenseAmount(BigDecimal.ZERO)
                .build();
        dsPlatformAccountMapper.insert(account);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordCommissionIncome(Long orderId, String orderNo, LocalDateTime occurredAt, List<DsOrderCommissionSplit> splits) {
        if (splits == null || splits.isEmpty()) {
            return;
        }
        DsPlatformAccount account = getOrCreateMainAccount();
        BigDecimal balance = account.getAvailableAmount() == null ? BigDecimal.ZERO : account.getAvailableAmount();
        BigDecimal totalIncome = BigDecimal.ZERO;
        List<DsPlatformAccountLedger> ledgers = new ArrayList<>(splits.size());
        for (DsOrderCommissionSplit split : splits) {
            BigDecimal income = split.getPlatformNetAmount() == null ? BigDecimal.ZERO : split.getPlatformNetAmount();
            if (income.signum() <= 0) {
                continue;
            }
            balance = balance.add(income);
            totalIncome = totalIncome.add(income);
            ledgers.add(DsPlatformAccountLedger.builder()
                    .accountId(account.getId())
                    .changeType(CHANGE_TYPE_INCOME)
                    .amount(income)
                    .balanceAfter(balance)
                    .bizType(BIZ_TYPE_SHOP_ORDER_COMMISSION)
                    .bizNo("PAC-" + split.getSplitNo())
                    .orderId(orderId)
                    .orderNo(orderNo)
                    .orderItemId(split.getOrderItemId())
                    .splitId(split.getId())
                    .occurredAt(occurredAt != null ? occurredAt : LocalDateTime.now())
                    .build());
        }
        if (ledgers.isEmpty()) {
            return;
        }
        account.setAvailableAmount(balance);
        account.setTotalIncomeAmount((account.getTotalIncomeAmount() == null ? BigDecimal.ZERO : account.getTotalIncomeAmount()).add(totalIncome));
        dsPlatformAccountMapper.updateById(account);
        dsPlatformAccountLedgerMapper.insertBatch(ledgers);
    }
}
