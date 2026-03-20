package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointLedgerMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsMerchantScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@Tag(name = "管理后台 - 电商积分流水")
@RestController
@RequestMapping("/ds/point-ledger")
@Validated
public class DsPointLedgerController {

    @Resource
    private DsPointLedgerMapper dsPointLedgerMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private DsMerchantScopeService dsMerchantScopeService;

    @GetMapping("/page")
    @Operation(summary = "积分流水分页")
    public CommonResult<PageResult<DsPointLedgerAdminRespVO>> getPage(PageParam pageParam,
                                                                       @RequestParam(value = "uid", required = false) Long uid,
                                                                       @RequestParam(value = "changeType", required = false) String changeType,
                                                                       @RequestParam(value = "bizType", required = false) String bizType) {
        Long actualUid = resolveQueryUid(uid);
        PageResult<DsPointLedger> ledgerPage = dsPointLedgerMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsPointLedger>()
                .eqIfPresent(DsPointLedger::getUid, actualUid)
                .eqIfPresent(DsPointLedger::getChangeType, changeType)
                .likeIfPresent(DsPointLedger::getBizType, bizType)
                .orderByDesc(DsPointLedger::getId));
        Set<Long> userIds = ledgerPage.getList().stream()
                .flatMap(item -> java.util.stream.Stream.of(item.getUid(), item.getSourceUid()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, DsUser> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                dsUserMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(DsUser::getId, Function.identity(), (left, right) -> left));
        List<DsPointLedgerAdminRespVO> result = ledgerPage.getList().stream().map(item -> {
            DsPointLedgerAdminRespVO vo = new DsPointLedgerAdminRespVO();
            BeanUtils.copyProperties(item, vo);
            DsUser user = userMap.get(item.getUid());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserMobile(user.getMobile());
            }
            DsUser sourceUser = userMap.get(item.getSourceUid());
            if (sourceUser != null) {
                vo.setSourceUserNickname(sourceUser.getNickname());
                vo.setSourceUserMobile(sourceUser.getMobile());
            }
            return vo;
        }).toList();
        return success(new PageResult<>(result, ledgerPage.getTotal()));
    }

    @GetMapping("/uid-filter-scope")
    @Operation(summary = "获得积分流水用户编号过滤范围")
    public CommonResult<DsPointLedgerUidFilterScopeRespVO> getUidFilterScope() {
        DsPointLedgerUidFilterScopeRespVO respVO = new DsPointLedgerUidFilterScopeRespVO();
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        respVO.setCanFilterUid(scope.getCanFilterUid());
        respVO.setDefaultUid(scope.getDefaultUid());
        return success(respVO);
    }

    private Long resolveQueryUid(Long requestedUid) {
        DsMerchantScopeService.DsMerchantScope scope = dsMerchantScopeService.getCurrentScope();
        return scope.getCanFilterUid() ? requestedUid : scope.getDefaultUid();
    }

    @Data
    public static class DsPointLedgerAdminRespVO extends DsPointLedger {

        private String userNickname;
        private String userMobile;
        private String sourceUserNickname;
        private String sourceUserMobile;
    }

    @Data
    public static class DsPointLedgerUidFilterScopeRespVO {

        private Boolean canFilterUid;
        private Long defaultUid;
    }
}
