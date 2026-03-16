package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;

import java.time.LocalDateTime;

public interface DsMembershipAccountService {

    DsMembershipAccount getAccount(Long uid);

    DsMembershipAccount getOrCreateAccount(Long uid);

    void activateMembership(Long uid, DsMembershipPlan plan, LocalDateTime paidAt);
}
