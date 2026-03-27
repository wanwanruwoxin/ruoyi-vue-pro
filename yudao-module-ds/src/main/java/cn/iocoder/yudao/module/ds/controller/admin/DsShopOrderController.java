package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.ds.controller.admin.order.vo.DsShopOrderItemRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.order.vo.DsShopOrderPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.order.vo.DsShopOrderRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrder;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderItemMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsMerchantScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - DS 商城订单")
@RestController
@RequestMapping("/ds/order")
@Validated
public class DsShopOrderController {

    @Resource
    private DsShopOrderMapper dsShopOrderMapper;
    @Resource
    private DsShopOrderItemMapper dsShopOrderItemMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/page")
    @Operation(summary = "订单分页")
    public CommonResult<PageResult<DsShopOrderRespVO>> getOrderPage(@Validated DsShopOrderPageReqVO reqVO) {
        PageResult<DsShopOrder> pageResult = queryOrderPage(reqVO);
        List<DsShopOrderRespVO> respList = convertOrderList(pageResult.getList());
        return success(new PageResult<>(respList, pageResult.getTotal()));
    }

    @GetMapping("/item/list")
    @Operation(summary = "订单项列表")
    @Parameter(name = "orderId", required = true, example = "1")
    public CommonResult<List<DsShopOrderItemRespVO>> getOrderItemList(@RequestParam("orderId") Long orderId) {
        Long actualShopId = resolveShopId(null);
        List<DsShopOrderItem> itemList = dsShopOrderItemMapper.selectListByOrderId(orderId, actualShopId);
        return success(BeanUtils.toBean(itemList, DsShopOrderItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderList(@Validated DsShopOrderPageReqVO reqVO, HttpServletResponse response) throws IOException {
        reqVO.setPageSize(PAGE_SIZE_NONE);
        List<DsShopOrderRespVO> list = convertOrderList(queryOrderPage(reqVO).getList());
        ExcelUtils.write(response, "商城订单.xls", "数据", DsShopOrderRespVO.class, list);
    }

    private PageResult<DsShopOrder> queryOrderPage(DsShopOrderPageReqVO reqVO) {
        reqVO.setUid(resolveUid(reqVO.getUid()));
        reqVO.setShopId(resolveShopId(reqVO.getShopId()));
        List<Long> filteredOrderIds = null;
        if (reqVO.getShopId() != null) {
            filteredOrderIds = dsShopOrderItemMapper.selectDistinctOrderIdsByShopId(reqVO.getShopId());
            if (filteredOrderIds.isEmpty()) {
                return new PageResult<>(Collections.emptyList(), 0L);
            }
        }
        return dsShopOrderMapper.selectAdminPage(reqVO, reqVO.getOrderNo(), reqVO.getUid(),
                reqVO.getPayStatus(), reqVO.getPaidAt(), filteredOrderIds);
    }

    private List<DsShopOrderRespVO> convertOrderList(List<DsShopOrder> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> orderIds = orders.stream().map(DsShopOrder::getId).collect(Collectors.toSet());
        Map<Long, DsShopOrderItem> orderItemMap = dsShopOrderItemMapper.selectList(DsShopOrderItem::getOrderId, orderIds).stream()
                .filter(item -> item.getOrderId() != null)
                .collect(Collectors.toMap(DsShopOrderItem::getOrderId, Function.identity(), (left, right) -> left, LinkedHashMap::new));
        Set<Long> shopIds = orderItemMap.values().stream().map(DsShopOrderItem::getShopId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DsShop> shopMap = shopIds.isEmpty() ? Collections.emptyMap() :
                dsShopMapper.selectList(DsShop::getId, shopIds).stream()
                        .collect(Collectors.toMap(DsShop::getId, Function.identity(), (left, right) -> left));
        Set<Long> userIds = orders.stream().map(DsShopOrder::getUid).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                dsUserMapper.selectList(DsUser::getId, userIds).stream()
                        .collect(Collectors.toMap(DsUser::getId, Function.identity(), (left, right) -> left));
        return orders.stream().map(order -> {
            DsShopOrderRespVO respVO = BeanUtils.toBean(order, DsShopOrderRespVO.class);
            DsShopOrderItem orderItem = orderItemMap.get(order.getId());
            if (orderItem != null) {
                respVO.setShopId(orderItem.getShopId());
                DsShop shop = shopMap.get(orderItem.getShopId());
                if (shop != null) {
                    respVO.setShopName(shop.getShopName());
                }
            }
            DsUser user = userMap.get(order.getUid());
            if (user != null) {
                respVO.setUserNickname(user.getNickname());
                respVO.setUserMobile(user.getMobile());
            }
            return respVO;
        }).toList();
    }

    private Long resolveUid(Long requestedUid) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return Boolean.TRUE.equals(scope.getCanFilterUid()) ? requestedUid : scope.getDefaultUid();
    }

    private Long resolveShopId(Long requestedShopId) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return Boolean.TRUE.equals(scope.getCanFilterShopId()) ? requestedShopId : scope.getDefaultShopId();
    }
}
