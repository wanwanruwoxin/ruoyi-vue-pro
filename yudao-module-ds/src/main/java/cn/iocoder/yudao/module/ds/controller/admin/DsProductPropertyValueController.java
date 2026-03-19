package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductPropertyValue;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductPropertyValueMapper;
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

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商商品属性值")
@RestController
@RequestMapping("/ds/product-property-value")
@Validated
public class DsProductPropertyValueController {

    @Resource
    private DsProductPropertyValueMapper dsProductPropertyValueMapper;

    @GetMapping("/page")
    @Operation(summary = "商品属性值分页")
    public CommonResult<PageResult<DsProductPropertyValue>> getPage(@Valid PageParam pageParam,
                                                                    @RequestParam(value = "propertyId", required = false) Long propertyId,
                                                                    @RequestParam(value = "name", required = false) String name) {
        return success(dsProductPropertyValueMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsProductPropertyValue>()
                .eqIfPresent(DsProductPropertyValue::getPropertyId, propertyId)
                .likeIfPresent(DsProductPropertyValue::getName, name)
                .orderByDesc(DsProductPropertyValue::getId)));
    }

    @GetMapping("/list")
    @Operation(summary = "商品属性值列表")
    public CommonResult<List<DsProductPropertyValue>> getList(@RequestParam(value = "propertyId", required = false) Long propertyId) {
        return success(dsProductPropertyValueMapper.selectList(new LambdaQueryWrapperX<DsProductPropertyValue>()
                .eqIfPresent(DsProductPropertyValue::getPropertyId, propertyId)
                .orderByDesc(DsProductPropertyValue::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "商品属性值详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsProductPropertyValue> get(@RequestParam("id") Long id) {
        return success(dsProductPropertyValueMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品属性值")
    public CommonResult<Long> create(@Valid @RequestBody DsProductPropertyValue reqVO) {
        reqVO.setId(null);
        dsProductPropertyValueMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品属性值")
    public CommonResult<Boolean> update(@Valid @RequestBody DsProductPropertyValue reqVO) {
        dsProductPropertyValueMapper.updateById(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品属性值")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductPropertyValueMapper.deleteById(id);
        return success(true);
    }
}
