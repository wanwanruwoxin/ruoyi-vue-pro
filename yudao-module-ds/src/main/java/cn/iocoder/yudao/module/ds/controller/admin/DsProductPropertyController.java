package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductProperty;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductPropertyMapper;
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

@Tag(name = "管理后台 - 电商商品属性项")
@RestController
@RequestMapping("/ds/product-property")
@Validated
public class DsProductPropertyController {

    @Resource
    private DsProductPropertyMapper dsProductPropertyMapper;

    @GetMapping("/page")
    @Operation(summary = "商品属性项分页")
    public CommonResult<PageResult<DsProductProperty>> getPage(@Valid PageParam pageParam,
                                                               @RequestParam(value = "name", required = false) String name) {
        return success(dsProductPropertyMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsProductProperty>()
                .likeIfPresent(DsProductProperty::getName, name)
                .orderByDesc(DsProductProperty::getId)));
    }

    @GetMapping("/list")
    @Operation(summary = "商品属性项列表")
    public CommonResult<List<DsProductProperty>> getList() {
        return success(dsProductPropertyMapper.selectList(new LambdaQueryWrapperX<DsProductProperty>()
                .orderByDesc(DsProductProperty::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "商品属性项详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsProductProperty> get(@RequestParam("id") Long id) {
        return success(dsProductPropertyMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品属性项")
    public CommonResult<Long> create(@Valid @RequestBody DsProductProperty reqVO) {
        reqVO.setId(null);
        dsProductPropertyMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品属性项")
    public CommonResult<Boolean> update(@Valid @RequestBody DsProductProperty reqVO) {
        dsProductPropertyMapper.updateById(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品属性项")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductPropertyMapper.deleteById(id);
        return success(true);
    }
}
