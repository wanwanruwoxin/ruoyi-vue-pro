package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipPlanMapper;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.ADVANCED;
import static cn.iocoder.yudao.module.ds.enums.DsMembershipConstants.PlanCode.NORMAL;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.MEMBERSHIP_PLAN_NOT_EXISTS;

@Service
@Validated
public class DsMembershipPlanServiceImpl implements DsMembershipPlanService {

    @Resource
    private DsMembershipPlanMapper dsMembershipPlanMapper;

    @Override
    public List<DsMembershipPlan> getAvailablePlans() {
        initDefaultPlansIfAbsent();
        return dsMembershipPlanMapper.selectListByStatus(CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public DsMembershipPlan getPlan(Long planId) {
        initDefaultPlansIfAbsent();
        DsMembershipPlan plan = dsMembershipPlanMapper.selectById(planId);
        if (plan == null || CommonStatusEnum.isDisable(plan.getStatus())) {
            throw exception(MEMBERSHIP_PLAN_NOT_EXISTS);
        }
        return plan;
    }

    @Override
    public DsMembershipPlan getPlanByCode(String planCode) {
        initDefaultPlansIfAbsent();
        DsMembershipPlan plan = dsMembershipPlanMapper.selectByPlanCode(planCode);
        if (plan == null || CommonStatusEnum.isDisable(plan.getStatus())) {
            throw exception(MEMBERSHIP_PLAN_NOT_EXISTS);
        }
        return plan;
    }

    @Transactional(rollbackFor = Exception.class)
    protected void initDefaultPlansIfAbsent() {
        initPlan(NORMAL.getCode(), "普通会员", new BigDecimal("199"), 365);
        initPlan(ADVANCED.getCode(), "高级会员", new BigDecimal("1990"), 365);
    }

    private void initPlan(String planCode, String planName, BigDecimal priceAmount, Integer durationDays) {
        if (dsMembershipPlanMapper.selectByPlanCode(planCode) != null) {
            return;
        }
        DsMembershipPlan plan = DsMembershipPlan.builder()
                .planCode(planCode)
                .planName(planName)
                .priceAmount(priceAmount)
                .durationDays(durationDays)
                .status(CommonStatusEnum.ENABLE.getStatus())
                .build();
        dsMembershipPlanMapper.insert(plan);
    }
}
