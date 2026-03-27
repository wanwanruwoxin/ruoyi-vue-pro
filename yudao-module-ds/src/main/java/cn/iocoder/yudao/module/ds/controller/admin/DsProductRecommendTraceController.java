package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.recommend.vo.DsProductRecommendTracePageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.recommend.vo.DsProductRecommendTraceRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductRecommendTrace;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductRecommendTraceMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 商品推荐跟踪")
@RestController
@RequestMapping("/ds/product-recommend-trace")
@Validated
public class DsProductRecommendTraceController {

    @Resource
    private DsProductRecommendTraceMapper dsProductRecommendTraceMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsProductMapper dsProductMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/page")
    @Operation(summary = "商品推荐跟踪分页")
    public CommonResult<PageResult<DsProductRecommendTraceRespVO>> getPage(@Validated DsProductRecommendTracePageReqVO reqVO) {
        reqVO.setShopId(resolveShopId(reqVO.getShopId()));
        PageResult<DsProductRecommendTrace> page = dsProductRecommendTraceMapper.selectPage(reqVO,
                new LambdaQueryWrapperX<DsProductRecommendTrace>()
                        .likeIfPresent(DsProductRecommendTrace::getTraceNo, reqVO.getTraceNo())
                        .eqIfPresent(DsProductRecommendTrace::getInviterId, reqVO.getInviterId())
                        .eqIfPresent(DsProductRecommendTrace::getInviteeId, reqVO.getInviteeId())
                        .eqIfPresent(DsProductRecommendTrace::getShopId, reqVO.getShopId())
                        .eqIfPresent(DsProductRecommendTrace::getProductId, reqVO.getProductId())
                        .eqIfPresent(DsProductRecommendTrace::getRecommendScene, reqVO.getRecommendScene())
                        .eqIfPresent(DsProductRecommendTrace::getRecommendStatus, reqVO.getRecommendStatus())
                        .likeIfPresent(DsProductRecommendTrace::getBindOrderNo, reqVO.getBindOrderNo())
                        .betweenIfPresent(DsProductRecommendTrace::getRecommendedAt, reqVO.getRecommendedAt())
                        .orderByDesc(DsProductRecommendTrace::getId));
        Set<Long> userIds = page.getList().stream()
                .flatMap(item -> java.util.stream.Stream.of(item.getInviterId(), item.getInviteeId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> shopIds = page.getList().stream()
                .map(DsProductRecommendTrace::getShopId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> productIds = page.getList().stream()
                .map(DsProductRecommendTrace::getProductId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                dsUserMapper.selectList(DsUser::getId, userIds).stream()
                        .collect(Collectors.toMap(DsUser::getId, Function.identity(), (left, right) -> left));
        Map<Long, DsShop> shopMap = shopIds.isEmpty() ? Collections.emptyMap() :
                dsShopMapper.selectList(DsShop::getId, shopIds).stream()
                        .collect(Collectors.toMap(DsShop::getId, Function.identity(), (left, right) -> left));
        Map<Long, DsProduct> productMap = productIds.isEmpty() ? Collections.emptyMap() :
                dsProductMapper.selectList(DsProduct::getId, productIds).stream()
                        .collect(Collectors.toMap(DsProduct::getId, Function.identity(), (left, right) -> left));
        return success(new PageResult<>(page.getList().stream().map(item -> {
            DsProductRecommendTraceRespVO vo = BeanUtils.toBean(item, DsProductRecommendTraceRespVO.class);
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
            DsShop shop = shopMap.get(item.getShopId());
            if (shop != null) {
                vo.setShopName(shop.getShopName());
            }
            DsProduct product = productMap.get(item.getProductId());
            if (product != null) {
                vo.setProductName(product.getProductName());
            }
            return vo;
        }).toList(), page.getTotal()));
    }

    private Long resolveShopId(Long requestedShopId) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return Boolean.TRUE.equals(scope.getCanFilterShopId()) ? requestedShopId : scope.getDefaultShopId();
    }
}
