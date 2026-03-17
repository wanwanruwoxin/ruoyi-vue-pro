package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DsPointAccountService {

    DsPointAccount getOrCreateAccount(Long uid);

    void earnPoints(Long uid, BigDecimal points, String bizType, String bizNo,
                    Long sourceUid, String rewardRuleVersion, LocalDateTime occurredAt);

    void spendPoints(Long uid, BigDecimal points, String bizType, String bizNo, LocalDateTime occurredAt);

    void giftPoints(Long uid, String targetMobile, BigDecimal points);
}
