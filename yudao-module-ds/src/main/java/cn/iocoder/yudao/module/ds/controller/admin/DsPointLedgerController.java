package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointLedger;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPointLedgerMapper;
import cn.iocoder.yudao.module.ds.service.DsPointLedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商积分流水")
@RestController
@RequestMapping("/ds/point-ledger")
@Validated
public class DsPointLedgerController {

    @Resource
    private DsPointLedgerService dsPointLedgerService;
    @Resource
    private DsPointLedgerMapper dsPointLedgerMapper;

    @GetMapping("/page")
    @Operation(summary = "积分流水分页")
    public CommonResult<PageResult<DsPointLedger>> getPage(PageParam pageParam,
                                                            @RequestParam(value = "uid", required = false) Long uid,
                                                            @RequestParam(value = "changeType", required = false) String changeType,
                                                            @RequestParam(value = "bizType", required = false) String bizType) {
        return success(dsPointLedgerMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsPointLedger>()
                .eqIfPresent(DsPointLedger::getUid, uid)
                .eqIfPresent(DsPointLedger::getChangeType, changeType)
                .likeIfPresent(DsPointLedger::getBizType, bizType)
                .orderByDesc(DsPointLedger::getId)));
    }
}
