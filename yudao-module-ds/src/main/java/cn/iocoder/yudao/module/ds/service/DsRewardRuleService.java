package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;

public interface DsRewardRuleService {

    DsRewardRule getMembershipInviteRewardRule();

    DsRewardRule getMembershipInviteRewardRuleByLevel(Integer relationLevel);

    DsRewardRule getMembershipInviteRewardRuleByInviterLevel(String inviterLevel);

    void validatePointConsumeScope(String bizType);
}
