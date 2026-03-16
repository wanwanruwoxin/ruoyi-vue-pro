package cn.iocoder.yudao.module.ds.controller.app.membership;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.membership.vo.AppDsMembershipAccountRespVO;
import cn.iocoder.yudao.module.ds.controller.app.membership.vo.AppDsMembershipCreateOrderReqVO;
import cn.iocoder.yudao.module.ds.controller.app.membership.vo.AppDsMembershipOrderActionReqVO;
import cn.iocoder.yudao.module.ds.controller.app.membership.vo.AppDsMembershipOrderRespVO;
import cn.iocoder.yudao.module.ds.controller.app.membership.vo.AppDsMembershipPlanRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.service.DsMembershipAccountService;
import cn.iocoder.yudao.module.ds.service.DsMembershipOrderService;
import cn.iocoder.yudao.module.ds.service.DsMembershipPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 会员")
@RestController
@RequestMapping("/ds/membership")
@Validated
public class AppDsMembershipController {

    @Resource
    private DsMembershipPlanService dsMembershipPlanService;
    @Resource
    private DsMembershipAccountService dsMembershipAccountService;
    @Resource
    private DsMembershipOrderService dsMembershipOrderService;

    @PostMapping("/plans")
    @Operation(summary = "获取会员档位")
    public CommonResult<List<AppDsMembershipPlanRespVO>> getPlans() {
        List<DsMembershipPlan> plans = dsMembershipPlanService.getAvailablePlans();
        List<AppDsMembershipPlanRespVO> result = plans.stream().map(plan -> {
            AppDsMembershipPlanRespVO respVO = new AppDsMembershipPlanRespVO();
            respVO.setId(plan.getId());
            respVO.setPlanCode(plan.getPlanCode());
            respVO.setPlanName(plan.getPlanName());
            respVO.setPriceAmount(plan.getPriceAmount());
            respVO.setDurationDays(plan.getDurationDays());
            return respVO;
        }).toList();
        return success(result);
    }

    @PostMapping("/account")
    @Operation(summary = "获取我的会员状态")
    public CommonResult<AppDsMembershipAccountRespVO> getAccount() {
        DsMembershipAccount account = dsMembershipAccountService.getAccount(getLoginUserId());
        AppDsMembershipAccountRespVO respVO = new AppDsMembershipAccountRespVO();
        respVO.setUid(account.getUid());
        respVO.setCurrentPlanCode(account.getCurrentPlanCode());
        respVO.setMemberStatus(account.getMemberStatus());
        respVO.setEffectiveTime(account.getEffectiveTime());
        respVO.setExpireTime(account.getExpireTime());
        return success(respVO);
    }

    @PostMapping("/order/create")
    @Operation(summary = "创建会员购买订单")
    public CommonResult<AppDsMembershipOrderRespVO> createOrder(@RequestBody @Valid AppDsMembershipCreateOrderReqVO reqVO) {
        DsMembershipOrder order = dsMembershipOrderService.createOrder(getLoginUserId(), reqVO.getPlanId());
        return success(buildOrderResp(order));
    }

    @PostMapping("/order/pay")
    @Operation(summary = "支付会员订单")
    public CommonResult<Boolean> payOrder(@RequestBody @Valid AppDsMembershipOrderActionReqVO reqVO) {
        dsMembershipOrderService.payOrder(getLoginUserId(), reqVO.getOrderNo());
        return success(true);
    }

    @PostMapping("/order/close")
    @Operation(summary = "关闭会员订单")
    public CommonResult<Boolean> closeOrder(@RequestBody @Valid AppDsMembershipOrderActionReqVO reqVO) {
        dsMembershipOrderService.closeOrder(getLoginUserId(), reqVO.getOrderNo());
        return success(true);
    }

    @PostMapping("/order/refund")
    @Operation(summary = "退款会员订单")
    public CommonResult<Boolean> refundOrder(@RequestBody @Valid AppDsMembershipOrderActionReqVO reqVO) {
        dsMembershipOrderService.refundOrder(getLoginUserId(), reqVO.getOrderNo());
        return success(true);
    }

    @PostMapping("/order/list")
    @Operation(summary = "获取我的会员订单")
    public CommonResult<List<AppDsMembershipOrderRespVO>> listOrders() {
        List<DsMembershipOrder> orders = dsMembershipOrderService.getUserOrders(getLoginUserId());
        List<AppDsMembershipOrderRespVO> result = orders.stream().map(this::buildOrderResp).toList();
        return success(result);
    }

    private AppDsMembershipOrderRespVO buildOrderResp(DsMembershipOrder order) {
        AppDsMembershipOrderRespVO respVO = new AppDsMembershipOrderRespVO();
        respVO.setOrderNo(order.getOrderNo());
        respVO.setPlanId(order.getPlanId());
        respVO.setPayableAmount(order.getPayableAmount());
        respVO.setPayStatus(order.getPayStatus());
        respVO.setPaidAt(order.getPaidAt());
        respVO.setRefundStatus(order.getRefundStatus());
        return respVO;
    }
}
