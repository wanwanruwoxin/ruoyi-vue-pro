package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;

import java.util.List;

public interface DsMembershipPlanService {

    List<DsMembershipPlan> getAvailablePlans();

    DsMembershipPlan getPlan(Long planId);

    DsMembershipPlan getPlanByCode(String planCode);
}
