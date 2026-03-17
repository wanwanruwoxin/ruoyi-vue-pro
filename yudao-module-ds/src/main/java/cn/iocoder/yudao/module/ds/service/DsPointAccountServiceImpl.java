package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointAccountMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointChangeType.EARN;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointChangeType.SPEND;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.POINT_ACCOUNT_INSUFFICIENT;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.POINT_GIFT_SELF_NOT_ALLOWED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.POINT_GIFT_TARGET_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.POINT_GIFT_RECEIVE;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PointBizType.POINT_GIFT_SEND;

@Service
@Validated
public class DsPointAccountServiceImpl implements DsPointAccountService {

    @Resource
    private DsPointAccountMapper dsPointAccountMapper;
    @Resource
    private DsPointLedgerService dsPointLedgerService;
    @Resource
    private DsRewardRuleService dsRewardRuleService;
    @Resource
    private DsUserMapper dsUserMapper;

    @Override
    public DsPointAccount getOrCreateAccount(Long uid) {
        DsPointAccount account = dsPointAccountMapper.selectByUid(uid);
        if (account != null) {
            return account;
        }
        account = DsPointAccount.builder()
                .uid(uid)
                .availablePoints(BigDecimal.ZERO)
                .frozenPoints(BigDecimal.ZERO)
                .totalEarnedPoints(BigDecimal.ZERO)
                .totalSpentPoints(BigDecimal.ZERO)
                .build();
        dsPointAccountMapper.insert(account);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void earnPoints(Long uid, BigDecimal points, String bizType, String bizNo,
                           Long sourceUid, String rewardRuleVersion, LocalDateTime occurredAt) {
        BigDecimal changedPoints = normalizePoints(points);
        if (changedPoints.signum() <= 0) {
            return;
        }
        DsPointAccount account = getOrCreateAccount(uid);
        account.setAvailablePoints(account.getAvailablePoints().add(changedPoints));
        account.setTotalEarnedPoints(account.getTotalEarnedPoints().add(changedPoints));
        dsPointAccountMapper.updateById(account);
        dsPointLedgerService.createLedger(DsPointLedger.builder()
                .uid(uid)
                .changeType(EARN.getCode())
                .points(changedPoints)
                .balanceAfter(account.getAvailablePoints())
                .bizType(bizType)
                .bizNo(bizNo)
                .sourceUid(sourceUid)
                .rewardRuleVersion(rewardRuleVersion)
                .occurredAt(occurredAt != null ? occurredAt : LocalDateTime.now())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void spendPoints(Long uid, BigDecimal points, String bizType, String bizNo, LocalDateTime occurredAt) {
        dsRewardRuleService.validatePointConsumeScope(bizType);
        BigDecimal changedPoints = normalizePoints(points);
        if (changedPoints.signum() <= 0) {
            return;
        }
        DsPointAccount account = getOrCreateAccount(uid);
        if (account.getAvailablePoints().compareTo(changedPoints) < 0) {
            throw exception(POINT_ACCOUNT_INSUFFICIENT);
        }
        account.setAvailablePoints(account.getAvailablePoints().subtract(changedPoints));
        account.setTotalSpentPoints(account.getTotalSpentPoints().add(changedPoints));
        dsPointAccountMapper.updateById(account);
        dsPointLedgerService.createLedger(DsPointLedger.builder()
                .uid(uid)
                .changeType(SPEND.getCode())
                .points(changedPoints.negate())
                .balanceAfter(account.getAvailablePoints())
                .bizType(bizType)
                .bizNo(bizNo)
                .sourceUid(null)
                .rewardRuleVersion(null)
                .occurredAt(occurredAt != null ? occurredAt : LocalDateTime.now())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void giftPoints(Long uid, String targetMobile, BigDecimal points) {
        DsUser targetUser = dsUserMapper.selectByMobile(targetMobile);
        if (targetUser == null) {
            throw exception(POINT_GIFT_TARGET_NOT_EXISTS);
        }
        Long targetUid = targetUser.getId();
        if (uid.equals(targetUid)) {
            throw exception(POINT_GIFT_SELF_NOT_ALLOWED);
        }
        BigDecimal changedPoints = normalizePoints(points);
        if (changedPoints.signum() <= 0) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String bizNo = "G" + IdUtil.fastSimpleUUID();
        spendPoints(uid, changedPoints, POINT_GIFT_SEND.getCode(), bizNo, now);
        earnPoints(targetUid, changedPoints, POINT_GIFT_RECEIVE.getCode(), bizNo, uid, null, now);
    }

    private BigDecimal normalizePoints(BigDecimal points) {
        if (points == null) {
            return BigDecimal.ZERO;
        }
        return points.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
