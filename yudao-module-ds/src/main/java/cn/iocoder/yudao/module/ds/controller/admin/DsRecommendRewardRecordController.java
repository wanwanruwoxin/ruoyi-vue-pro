package cn.iocoder.yudao.module.ds.controller.admin;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.recommend.vo.DsRecommendRewardRecordPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.recommend.vo.DsRecommendRewardRecordRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRecommendRewardRecord;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShopOrderItem;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsRecommendRewardRecordMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopOrderItemMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsMerchantScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

@Tag(name = "管理后台 - 推荐奖励记录")
@RestController
@RequestMapping("/ds/recommend-reward-record")
@Validated
public class DsRecommendRewardRecordController {

    @Resource
    private DsRecommendRewardRecordMapper dsRecommendRewardRecordMapper;
    @Resource
    private DsShopOrderItemMapper dsShopOrderItemMapper;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/page")
    @Operation(summary = "推荐奖励记录分页")
    public CommonResult<PageResult<DsRecommendRewardRecordRespVO>> getPage(@Validated DsRecommendRewardRecordPageReqVO reqVO) {
        Long actualShopId = resolveShopId(reqVO.getShopId());
        List<Long> filteredOrderItemIds = null;
        if (actualShopId != null) {
            filteredOrderItemIds = dsShopOrderItemMapper.selectOrderItemIdsByShopId(actualShopId);
            if (CollUtil.isEmpty(filteredOrderItemIds)) {
                return success(new PageResult<>(Collections.emptyList(), 0L));
            }
        }
        PageResult<DsRecommendRewardRecord> page = dsRecommendRewardRecordMapper.selectPage(reqVO,
                new LambdaQueryWrapperX<DsRecommendRewardRecord>()
                        .likeIfPresent(DsRecommendRewardRecord::getRewardNo, reqVO.getRewardNo())
                        .likeIfPresent(DsRecommendRewardRecord::getOrderNo, reqVO.getOrderNo())
                        .likeIfPresent(DsRecommendRewardRecord::getTraceNo, reqVO.getTraceNo())
                        .eqIfPresent(DsRecommendRewardRecord::getInviterId, reqVO.getInviterId())
                        .eqIfPresent(DsRecommendRewardRecord::getInviteeId, reqVO.getInviteeId())
                        .eqIfPresent(DsRecommendRewardRecord::getGrantStatus, reqVO.getGrantStatus())
                        .betweenIfPresent(DsRecommendRewardRecord::getOccurredAt, reqVO.getOccurredAt())
                        .inIfPresent(DsRecommendRewardRecord::getOrderItemId, filteredOrderItemIds)
                        .orderByDesc(DsRecommendRewardRecord::getId));
        Set<Long> orderItemIds = page.getList().stream()
                .map(DsRecommendRewardRecord::getOrderItemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsShopOrderItem> orderItemMap = orderItemIds.isEmpty() ? Collections.emptyMap() :
                dsShopOrderItemMapper.selectList(DsShopOrderItem::getId, orderItemIds).stream()
                        .collect(Collectors.toMap(DsShopOrderItem::getId, Function.identity(), (left, right) -> left));
        Set<Long> userIds = page.getList().stream()
                .flatMap(item -> java.util.stream.Stream.of(item.getInviterId(), item.getInviteeId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                dsUserMapper.selectList(DsUser::getId, userIds).stream()
                        .collect(Collectors.toMap(DsUser::getId, Function.identity(), (left, right) -> left));
        Set<Long> shopIds = orderItemMap.values().stream()
                .map(DsShopOrderItem::getShopId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsShop> shopMap = shopIds.isEmpty() ? Collections.emptyMap() :
                dsShopMapper.selectList(DsShop::getId, shopIds).stream()
                        .collect(Collectors.toMap(DsShop::getId, Function.identity(), (left, right) -> left));
        return success(new PageResult<>(page.getList().stream().map(item -> {
            DsRecommendRewardRecordRespVO vo = BeanUtils.toBean(item, DsRecommendRewardRecordRespVO.class);
            DsUser inviter = userMap.get(item.getInviterId());
            if (inviter != null) {
                vo.setInviterNickname(inviter.getNickname());
                vo.setInviterMobile(inviter.getMobile());
            }
            DsUser invitee = userMap.get(item.getInviteeId());
            if (invitee != null) {
                vo.setInviteeNickname(invitee.getNickname());
                vo.setInviteeMobile(invitee.getMobile());
            }
            DsShopOrderItem orderItem = orderItemMap.get(item.getOrderItemId());
            if (orderItem != null) {
                vo.setShopId(orderItem.getShopId());
                vo.setProductId(orderItem.getProductId());
                vo.setProductName(orderItem.getProductName());
                DsShop shop = shopMap.get(orderItem.getShopId());
                if (shop != null) {
                    vo.setShopName(shop.getShopName());
                }
            }
            return vo;
        }).toList(), page.getTotal()));
    }

    private Long resolveShopId(Long requestedShopId) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return Boolean.TRUE.equals(scope.getCanFilterShopId()) ? requestedShopId : scope.getDefaultShopId();
    }
}
