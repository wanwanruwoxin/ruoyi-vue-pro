package cn.iocoder.yudao.module.ds.controller.app.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.service.DsShopOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 商城订单")
@RestController
@RequestMapping("/ds/order")
public class AppDsShopOrderController {

    @Resource
    private DsShopOrderService dsShopOrderService;

    @PostMapping("/create-and-pay")
    @Operation(summary = "创建并积分支付订单")
    public CommonResult<AppDsShopOrderRespVO> createAndPay(@Valid @RequestBody AppDsShopOrderCreateReqVO reqVO) {
        DsShopOrder order = dsShopOrderService.createAndPayOrder(getLoginUserId(), reqVO);
        return success(convert(order));
    }

    @PostMapping("/list")
    @Operation(summary = "查询我的订单列表")
    public CommonResult<List<AppDsShopOrderRespVO>> list() {
        List<DsShopOrder> orders = dsShopOrderService.getUserOrders(getLoginUserId());
        return success(orders.stream().map(this::convert).toList());
    }

    private AppDsShopOrderRespVO convert(DsShopOrder order) {
        AppDsShopOrderRespVO respVO = new AppDsShopOrderRespVO();
        respVO.setOrderNo(order.getOrderNo());
        respVO.setItemCount(order.getItemCount());
        respVO.setTotalAmount(order.getTotalAmount());
        respVO.setPayPoints(order.getPayPoints());
        respVO.setPayStatus(order.getPayStatus());
        respVO.setPaidAt(order.getPaidAt());
        respVO.setProductSummary(order.getProductSummary());
        return respVO;
    }
}
