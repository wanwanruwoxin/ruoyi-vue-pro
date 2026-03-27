package cn.iocoder.yudao.module.ds.controller.admin;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo.DsPlatformAccountLedgerPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo.DsPlatformAccountLedgerRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccount;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccountLedger;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPlatformAccountLedgerMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPlatformAccountMapper;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - DS 平台账户流水")
@RestController
@RequestMapping("/ds/platform-account-ledger")
@Validated
public class DsPlatformAccountLedgerController {

    @Resource
    private DsPlatformAccountLedgerMapper dsPlatformAccountLedgerMapper;
    @Resource
    private DsPlatformAccountMapper dsPlatformAccountMapper;

    @GetMapping("/page")
    @Operation(summary = "平台账户流水分页")
    public CommonResult<PageResult<DsPlatformAccountLedgerRespVO>> getPlatformAccountLedgerPage(@Validated DsPlatformAccountLedgerPageReqVO reqVO) {
        PageResult<DsPlatformAccountLedger> pageResult = dsPlatformAccountLedgerMapper.selectPage(reqVO, new LambdaQueryWrapperX<DsPlatformAccountLedger>()
                .eqIfPresent(DsPlatformAccountLedger::getAccountId, reqVO.getAccountId())
                .eqIfPresent(DsPlatformAccountLedger::getChangeType, reqVO.getChangeType())
                .eqIfPresent(DsPlatformAccountLedger::getBizType, reqVO.getBizType())
                .likeIfPresent(DsPlatformAccountLedger::getBizNo, reqVO.getBizNo())
                .likeIfPresent(DsPlatformAccountLedger::getOrderNo, reqVO.getOrderNo())
                .betweenIfPresent(DsPlatformAccountLedger::getOccurredAt, reqVO.getOccurredAt())
                .orderByDesc(DsPlatformAccountLedger::getId));
        List<DsPlatformAccountLedgerRespVO> list = BeanUtils.toBean(pageResult.getList(), DsPlatformAccountLedgerRespVO.class);
        fillAccountCode(list);
        return success(new PageResult<>(list, pageResult.getTotal()));
    }

    private void fillAccountCode(List<DsPlatformAccountLedgerRespVO> list) {
        Set<Long> accountIds = list.stream().map(DsPlatformAccountLedgerRespVO::getAccountId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(accountIds)) {
            return;
        }
        Map<Long, DsPlatformAccount> accountMap = dsPlatformAccountMapper.selectList(DsPlatformAccount::getId, accountIds).stream()
                .collect(Collectors.toMap(DsPlatformAccount::getId, Function.identity(), (left, right) -> left));
        list.forEach(item -> {
            DsPlatformAccount account = accountMap.get(item.getAccountId());
            item.setAccountCode(account == null ? null : account.getAccountCode());
        });
    }
}
