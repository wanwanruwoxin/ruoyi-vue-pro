package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;

import java.time.LocalDateTime;
import java.util.List;

public interface DsMembershipAccountService {

    DsMembershipAccount getAccount(Long uid);

    DsMembershipAccount getOrCreateAccount(Long uid);

    DsMembershipAccount getAccountIfPresent(Long uid);

    void activateMembership(Long uid, DsMembershipPlan plan, LocalDateTime paidAt);

    boolean isTeamLeader(Long uid);

    boolean isShareholder(Long uid);

    void markAsTeamLeader(Long uid);

    List<Long> listActiveShareholderUids();
}
