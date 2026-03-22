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
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.ACTIVE;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.EXPIRED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.MemberStatus.UNOPENED;

@Service
@Validated
public class DsMembershipAccountServiceImpl implements DsMembershipAccountService {
    private static final Integer YES = 1;


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
                .teamLeader(0)
                .shareholder(0)
                .build();
        dsMembershipAccountMapper.insert(account);
        return account;
    }

    @Override
    public DsMembershipAccount getAccountIfPresent(Long uid) {
        DsMembershipAccount account = dsMembershipAccountMapper.selectByUid(uid);
        if (account == null) {
            return null;
        }
        refreshMemberStatus(account);
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

    @Override
    public boolean isTeamLeader(Long uid) {
        DsMembershipAccount account = getAccountIfPresent(uid);
        return account != null && YES.equals(account.getTeamLeader());
    }

    @Override
    public boolean isShareholder(Long uid) {
        DsMembershipAccount account = getAccountIfPresent(uid);
        return account != null && YES.equals(account.getShareholder());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsTeamLeader(Long uid) {
        DsMembershipAccount account = getOrCreateAccount(uid);
        if (YES.equals(account.getTeamLeader())) {
            return;
        }
        account.setTeamLeader(YES);
        dsMembershipAccountMapper.updateById(account);
    }

    @Override
    public List<Long> listActiveShareholderUids() {
        return dsMembershipAccountMapper.selectListByShareholderAndMemberStatus(YES, ACTIVE.getCode()).stream()
                .map(DsMembershipAccount::getUid)
                .collect(Collectors.toList());
    }

    @Override
    public int countByUidsAndPlanCode(List<Long> uids, String planCode) {
        return dsMembershipAccountMapper.selectCountByUidsAndPlanCode(uids, planCode);
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
