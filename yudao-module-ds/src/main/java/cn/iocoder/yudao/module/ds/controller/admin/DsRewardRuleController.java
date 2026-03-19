package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsRewardRule;
import cn.iocoder.yudao.module.ds.dal.mysql.DsRewardRuleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商奖励规则")
@RestController
@RequestMapping("/ds/reward-rule")
@Validated
public class DsRewardRuleController {

    @Resource
    private DsRewardRuleMapper dsRewardRuleMapper;

    @GetMapping("/page")
    @Operation(summary = "奖励规则分页")
    public CommonResult<PageResult<DsRewardRule>> getPage(@Valid PageParam pageParam,
                                                          @RequestParam(value = "ruleVersion", required = false) String ruleVersion,
                                                          @RequestParam(value = "triggerEvent", required = false) String triggerEvent,
                                                          @RequestParam(value = "applicableInviterLevel", required = false) String applicableInviterLevel,
                                                          @RequestParam(value = "status", required = false) Integer status) {
        return success(dsRewardRuleMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsRewardRule>()
                .likeIfPresent(DsRewardRule::getRuleVersion, ruleVersion)
                .eqIfPresent(DsRewardRule::getTriggerEvent, triggerEvent)
                .eqIfPresent(DsRewardRule::getApplicableInviterLevel, applicableInviterLevel)
                .eqIfPresent(DsRewardRule::getStatus, status)
                .orderByDesc(DsRewardRule::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "奖励规则详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsRewardRule> get(@RequestParam("id") Long id) {
        return success(dsRewardRuleMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建奖励规则")
    public CommonResult<Long> create(@Valid @RequestBody DsRewardRule reqVO) {
        reqVO.setId(null);
        dsRewardRuleMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新奖励规则")
    public CommonResult<Boolean> update(@Valid @RequestBody DsRewardRule reqVO) {
        dsRewardRuleMapper.updateById(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除奖励规则")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsRewardRuleMapper.deleteById(id);
        return success(true);
    }
}
