package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsTeamConfig;
import cn.iocoder.yudao.module.ds.service.DsTeamConfigService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 团队奖励配置")
@RestController
@RequestMapping("/ds/team-config")
@Validated
public class DsTeamConfigController {

    @Resource
    private DsTeamConfigService dsTeamConfigService;

    @GetMapping("/page")
    @Operation(summary = "团队奖励配置分页")
    public CommonResult<PageResult<DsTeamConfig>> getPage(@Valid PageParam pageParam,
                                                           @RequestParam(value = "configKey", required = false) String configKey,
                                                           @RequestParam(value = "configName", required = false) String configName,
                                                           @RequestParam(value = "configGroup", required = false) String configGroup,
                                                           @RequestParam(value = "status", required = false) Integer status) {
        return success(dsTeamConfigService.getPage(pageParam, configKey, configName, configGroup, status));
    }

    @GetMapping("/get")
    @Operation(summary = "团队奖励配置详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsTeamConfig> get(@RequestParam("id") Long id) {
        return success(dsTeamConfigService.get(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建团队奖励配置")
    public CommonResult<Long> create(@Valid @RequestBody DsTeamConfig reqVO) {
        return success(dsTeamConfigService.create(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新团队奖励配置")
    public CommonResult<Boolean> update(@Valid @RequestBody DsTeamConfig reqVO) {
        dsTeamConfigService.update(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除团队奖励配置")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsTeamConfigService.delete(id);
        return success(true);
    }
}
