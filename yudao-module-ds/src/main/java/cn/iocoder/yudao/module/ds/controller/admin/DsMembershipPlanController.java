package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsMembershipPlan;
import cn.iocoder.yudao.module.ds.dal.mysql.DsMembershipPlanMapper;
import cn.iocoder.yudao.module.ds.service.DsMembershipPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商会员方案")
@RestController
@RequestMapping("/ds/membership-plan")
@Validated
public class DsMembershipPlanController {

    @Resource
    private DsMembershipPlanService dsMembershipPlanService;
    @Resource
    private DsMembershipPlanMapper dsMembershipPlanMapper;

    @GetMapping("/page")
    @Operation(summary = "会员方案分页")
    public CommonResult<PageResult<DsMembershipPlan>> getPage(@Valid PageParam pageParam,
                                                               @RequestParam(value = "planCode", required = false) String planCode,
                                                               @RequestParam(value = "planName", required = false) String planName,
                                                               @RequestParam(value = "status", required = false) Integer status) {
        return success(dsMembershipPlanMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsMembershipPlan>()
                .likeIfPresent(DsMembershipPlan::getPlanCode, planCode)
                .likeIfPresent(DsMembershipPlan::getPlanName, planName)
                .eqIfPresent(DsMembershipPlan::getStatus, status)
                .orderByDesc(DsMembershipPlan::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "会员方案详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsMembershipPlan> get(@RequestParam("id") Long id) {
        return success(dsMembershipPlanMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建会员方案")
    public CommonResult<Long> create(@Valid @RequestBody DsMembershipPlan reqVO) {
        reqVO.setId(null);
        dsMembershipPlanMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员方案")
    public CommonResult<Boolean> update(@Valid @RequestBody DsMembershipPlan reqVO) {
        dsMembershipPlanMapper.updateById(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员方案")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsMembershipPlanMapper.deleteById(id);
        return success(true);
    }
}
