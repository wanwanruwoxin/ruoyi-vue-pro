package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;

import java.math.BigDecimal;
import java.util.List;

public interface DsRewardRuleService {

    DsRewardRule getMembershipInviteRewardRule();

    DsRewardRule getMembershipInviteRewardRuleByLevel(Integer relationLevel);

    DsRewardRule getMembershipInviteRewardRuleByLevel(Integer relationLevel, String planCode);

    DsRewardRule getMembershipInviteRewardRuleByInviterLevel(String inviterLevel);

    DsRewardRule getMembershipInviteRewardRuleByInviterLevel(String inviterLevel, String planCode);

    List<DsRewardRule> listMembershipInviteRewardRules(String planCode);

    void validatePointConsumeScope(String bizType);

    ShopOrderCommissionRuleConfig getShopOrderCommissionRuleConfig();

    record ShopOrderCommissionRuleConfig(BigDecimal platformRate, BigDecimal recommendRewardRate,
                                         String platformRuleVersion, String recommendRuleVersion) {
    }
}
