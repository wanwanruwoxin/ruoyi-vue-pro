package cn.iocoder.yudao.module.ds.controller.app.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderCreateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.order.vo.AppDsShopOrderRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.service.DsShopOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 商城订单")
@RestController
@RequestMapping("/ds/order")
public class AppDsShopOrderController {

    @Resource
    private DsShopOrderService dsShopOrderService;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsProductMapper dsProductMapper;

    @PostMapping("/create-and-pay")
    @Operation(summary = "创建并积分支付订单")
    public CommonResult<AppDsShopOrderRespVO> createAndPay(@Valid @RequestBody AppDsShopOrderCreateReqVO reqVO) {
        DsShopOrder order = dsShopOrderService.createAndPayOrder(getLoginUserId(), reqVO);
        return success(convert(order));
    }

    @PostMapping("/list")
    @Operation(summary = "查询我的订单列表")
    public CommonResult<List<AppDsShopOrderRespVO>> list() {
        Long uid = getLoginUserId();
        List<DsShopOrder> orders = dsShopOrderService.getUserOrders(uid);
        Map<Long, DsShopOrder> orderMap = orders.stream()
                .filter(item -> item.getId() != null)
                .collect(Collectors.toMap(DsShopOrder::getId, Function.identity(), (left, right) -> left));
        List<DsShopOrderItem> orderItems = dsShopOrderService.getUserOrderItems(uid);
        Set<Long> shopIds = orderItems.stream()
                .map(DsShopOrderItem::getShopId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> productIds = orderItems.stream()
                .map(DsShopOrderItem::getProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsShop> shopMap = shopIds.isEmpty() ? Collections.emptyMap() :
                dsShopMapper.selectList(DsShop::getId, shopIds).stream()
                        .collect(Collectors.toMap(DsShop::getId, Function.identity(), (left, right) -> left));
        Map<Long, DsProduct> productMap = productIds.isEmpty() ? Collections.emptyMap() :
                dsProductMapper.selectList(DsProduct::getId, productIds).stream()
                        .collect(Collectors.toMap(DsProduct::getId, Function.identity(), (left, right) -> left));
        return success(orderItems.stream().map(item -> convert(item, orderMap, shopMap, productMap)).toList());
    }

    private AppDsShopOrderRespVO convert(DsShopOrder order) {
        AppDsShopOrderRespVO respVO = new AppDsShopOrderRespVO();
        respVO.setOrderNo(order.getOrderNo());
        respVO.setPayPoints(order.getPayPoints());
        respVO.setPayStatus(order.getPayStatus());
        respVO.setPaidAt(order.getPaidAt());
        respVO.setQuantity(order.getItemCount());
        return respVO;
    }

    private AppDsShopOrderRespVO convert(DsShopOrderItem item, Map<Long, DsShopOrder> orderMap,
                                         Map<Long, DsShop> shopMap, Map<Long, DsProduct> productMap) {
        AppDsShopOrderRespVO respVO = new AppDsShopOrderRespVO();
        respVO.setOrderNo(item.getOrderNo());
        respVO.setOrderItemId(item.getId());
        respVO.setPayPoints(item.getLineAmount());
        DsShopOrder order = orderMap.get(item.getOrderId());
        respVO.setPayStatus(order == null ? "PAID" : order.getPayStatus());
        respVO.setPaidAt(item.getPaidAt() == null && order != null ? order.getPaidAt() : item.getPaidAt());
        respVO.setShopId(item.getShopId());
        respVO.setProductId(item.getProductId());
        respVO.setProductName(item.getProductName());
        respVO.setProductSpec("默认规格");
        respVO.setQuantity(item.getQuantity());
        DsShop shop = shopMap.get(item.getShopId());
        if (shop != null) {
            respVO.setShopName(shop.getShopName());
        }
        DsProduct product = productMap.get(item.getProductId());
        if (product != null) {
            respVO.setProductImage(product.getPicUrl());
        }
        return respVO;
    }
}
