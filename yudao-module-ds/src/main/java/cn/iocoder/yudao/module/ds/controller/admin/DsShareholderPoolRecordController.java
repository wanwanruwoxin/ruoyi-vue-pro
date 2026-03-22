package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.shareholder.vo.DsShareholderPoolRecordPageReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShareholderPoolRecord;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShareholderPoolRecordMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsMerchantScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 股东池")
@RestController
@RequestMapping("/ds/shareholder-pool")
@Validated
public class DsShareholderPoolRecordController {

    private static final String SETTLE_STATUS_PENDING = "PENDING";
    private static final String SETTLE_STATUS_SETTLED = "SETTLED";

    @Resource
    private DsShareholderPoolRecordMapper dsShareholderPoolRecordMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/page")
    @Operation(summary = "股东池记录分页")
    public CommonResult<PageResult<DsShareholderPoolRecordRespVO>> getPage(@Valid DsShareholderPoolRecordPageReqVO reqVO) {
        Long actualSourceUid = resolveQueryUid(reqVO.getSourceUid());
        PageResult<DsShareholderPoolRecord> page = dsShareholderPoolRecordMapper.selectPage(reqVO,
                new LambdaQueryWrapperX<DsShareholderPoolRecord>()
                        .eqIfPresent(DsShareholderPoolRecord::getSourceUid, actualSourceUid)
                        .eqIfPresent(DsShareholderPoolRecord::getSourceType, reqVO.getSourceType())
                        .likeIfPresent(DsShareholderPoolRecord::getSourceOrderNo, reqVO.getSourceOrderNo())
                        .eqIfPresent(DsShareholderPoolRecord::getSettleMonth, reqVO.getSettleMonth())
                        .eqIfPresent(DsShareholderPoolRecord::getSettleStatus, reqVO.getSettleStatus())
                        .betweenIfPresent(DsShareholderPoolRecord::getOccurredAt, reqVO.getOccurredAt())
                        .orderByDesc(DsShareholderPoolRecord::getId));
        Set<Long> userIds = page.getList().stream()
                .map(DsShareholderPoolRecord::getSourceUid)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                dsUserMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(DsUser::getId, Function.identity(), (left, right) -> left));
        List<DsShareholderPoolRecordRespVO> result = page.getList().stream().map(item -> {
            DsShareholderPoolRecordRespVO vo = new DsShareholderPoolRecordRespVO();
            BeanUtils.copyProperties(item, vo);
            DsUser user = userMap.get(item.getSourceUid());
            if (user != null) {
                vo.setSourceUserNickname(user.getNickname());
                vo.setSourceUserMobile(user.getMobile());
            }
            return vo;
        }).toList();
        return success(new PageResult<>(result, page.getTotal()));
    }

    @GetMapping("/summary")
    @Operation(summary = "股东池汇总")
    public CommonResult<DsShareholderPoolSummaryRespVO> getSummary(
            @RequestParam(value = "sourceUid", required = false) Long sourceUid,
            @RequestParam(value = "settleMonth", required = false) String settleMonth) {
        Long actualSourceUid = resolveQueryUid(sourceUid);
        List<DsShareholderPoolRecord> records = dsShareholderPoolRecordMapper.selectList(
                new LambdaQueryWrapperX<DsShareholderPoolRecord>()
                        .eqIfPresent(DsShareholderPoolRecord::getSourceUid, actualSourceUid)
                        .eqIfPresent(DsShareholderPoolRecord::getSettleMonth, settleMonth));
        DsShareholderPoolSummaryRespVO respVO = new DsShareholderPoolSummaryRespVO();
        respVO.setTotalPoolAmount(sumPoolAmount(records, null));
        respVO.setPendingPoolAmount(sumPoolAmount(records, SETTLE_STATUS_PENDING));
        respVO.setSettledPoolAmount(sumPoolAmount(records, SETTLE_STATUS_SETTLED));
        respVO.setTotalCount((long) records.size());
        respVO.setPendingCount(records.stream().filter(item -> SETTLE_STATUS_PENDING.equals(item.getSettleStatus())).count());
        respVO.setSettledCount(records.stream().filter(item -> SETTLE_STATUS_SETTLED.equals(item.getSettleStatus())).count());
        return success(respVO);
    }

    @GetMapping("/uid-filter-scope")
    @Operation(summary = "获得股东池来源用户编号过滤范围")
    public CommonResult<DsShareholderPoolUidFilterScopeRespVO> getUidFilterScope() {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        DsShareholderPoolUidFilterScopeRespVO respVO = new DsShareholderPoolUidFilterScopeRespVO();
        respVO.setCanFilterUid(scope.getCanFilterUid());
        respVO.setDefaultUid(scope.getDefaultUid());
        return success(respVO);
    }

    private Long resolveQueryUid(Long requestedUid) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return scope.getCanFilterUid() ? requestedUid : scope.getDefaultUid();
    }

    private static BigDecimal sumPoolAmount(List<DsShareholderPoolRecord> records, String settleStatus) {
        return records.stream()
                .filter(item -> settleStatus == null || settleStatus.equals(item.getSettleStatus()))
                .map(DsShareholderPoolRecord::getPoolAmount)
                .filter(amount -> amount != null && amount.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Data
    public static class DsShareholderPoolRecordRespVO extends DsShareholderPoolRecord {

        private String sourceUserNickname;
        private String sourceUserMobile;
    }

    @Data
    public static class DsShareholderPoolSummaryRespVO {

        private BigDecimal totalPoolAmount;
        private BigDecimal pendingPoolAmount;
        private BigDecimal settledPoolAmount;
        private Long totalCount;
        private Long pendingCount;
        private Long settledCount;
    }

    @Data
    public static class DsShareholderPoolUidFilterScopeRespVO {

        private Boolean canFilterUid;
        private Long defaultUid;
    }
}
