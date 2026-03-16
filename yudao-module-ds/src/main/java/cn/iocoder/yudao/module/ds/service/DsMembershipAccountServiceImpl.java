package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipAccountMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.ACTIVE;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.EXPIRED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.UNOPENED;

@Service
@Validated
public class DsMembershipAccountServiceImpl implements DsMembershipAccountService {

    @Resource
    private DsMembershipAccountMapper dsMembershipAccountMapper;

    @Override
    public DsMembershipAccount getAccount(Long uid) {
        DsMembershipAccount account = getOrCreateAccount(uid);
        refreshMemberStatus(account);
        return account;
    }

    @Override
    public DsMembershipAccount getOrCreateAccount(Long uid) {
        DsMembershipAccount account = dsMembershipAccountMapper.selectByUid(uid);
        if (account != null) {
            return account;
        }
        account = DsMembershipAccount.builder()
                .uid(uid)
                .memberStatus(UNOPENED.getCode())
                .build();
        dsMembershipAccountMapper.insert(account);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateMembership(Long uid, DsMembershipPlan plan, LocalDateTime paidAt) {
        DsMembershipAccount account = getOrCreateAccount(uid);
        refreshMemberStatus(account);
        LocalDateTime now = paidAt != null ? paidAt : LocalDateTime.now();
        LocalDateTime start = now;
        if (ACTIVE.getCode().equals(account.getMemberStatus()) && account.getExpireTime() != null
                && account.getExpireTime().isAfter(now)) {
            start = account.getExpireTime();
        }
        LocalDateTime effectiveTime = account.getEffectiveTime();
        if (effectiveTime == null) {
            effectiveTime = now;
        }
        account.setCurrentPlanCode(plan.getPlanCode());
        account.setMemberStatus(ACTIVE.getCode());
        account.setEffectiveTime(effectiveTime);
        account.setExpireTime(start.plusDays(plan.getDurationDays()));
        dsMembershipAccountMapper.updateById(account);
    }

    private void refreshMemberStatus(DsMembershipAccount account) {
        LocalDateTime now = LocalDateTime.now();
        String status = account.getMemberStatus();
        LocalDateTime effectiveTime = account.getEffectiveTime();
        LocalDateTime expireTime = account.getExpireTime();
        if (expireTime != null && !expireTime.isAfter(now)) {
            if (!EXPIRED.getCode().equals(status)) {
                account.setMemberStatus(EXPIRED.getCode());
                dsMembershipAccountMapper.updateById(account);
            }
            return;
        }
        if (StrUtil.isNotBlank(account.getCurrentPlanCode()) && effectiveTime != null
                && !effectiveTime.isAfter(now) && expireTime != null && expireTime.isAfter(now)) {
            if (!ACTIVE.getCode().equals(status)) {
                account.setMemberStatus(ACTIVE.getCode());
                dsMembershipAccountMapper.updateById(account);
            }
            return;
        }
        if (StrUtil.isBlank(account.getCurrentPlanCode()) && !UNOPENED.getCode().equals(status)) {
            account.setMemberStatus(UNOPENED.getCode());
            dsMembershipAccountMapper.updateById(account);
        }
    }
}
