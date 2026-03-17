package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;

public interface DsRewardRuleService {

    DsRewardRule getMembershipInviteRewardRule();

    void validatePointConsumeScope(String bizType);
}
