 package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductCategory;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductCategoryMapper;
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

@Tag(name = "管理后台 - 电商商品分类")
@RestController
@RequestMapping("/ds/product-category")
@Validated
public class DsProductCategoryController {

    @Resource
    private DsProductCategoryMapper dsProductCategoryMapper;

    @GetMapping("/page")
    @Operation(summary = "商品分类分页")
    public CommonResult<PageResult<DsProductCategory>> getPage(@Valid PageParam pageParam,
                                                               @RequestParam(value = "name", required = false) String name,
                                                               @RequestParam(value = "status", required = false) Integer status,
                                                               @RequestParam(value = "parentId", required = false) Long parentId) {
        return success(dsProductCategoryMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsProductCategory>()
                .likeIfPresent(DsProductCategory::getName, name)
                .eqIfPresent(DsProductCategory::getStatus, status)
                .eqIfPresent(DsProductCategory::getParentId, parentId)
                .orderByAsc(DsProductCategory::getSort)
                .orderByDesc(DsProductCategory::getId)));
    }

    @GetMapping("/list")
    @Operation(summary = "商品分类列表")
    public CommonResult<List<DsProductCategory>> getList(@RequestParam(value = "status", required = false) Integer status) {
        return success(dsProductCategoryMapper.selectList(new LambdaQueryWrapperX<DsProductCategory>()
                .eqIfPresent(DsProductCategory::getStatus, status)
                .orderByAsc(DsProductCategory::getSort)
                .orderByDesc(DsProductCategory::getId)));
    }

    @GetMapping("/get")
    @Operation(summary = "商品分类详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsProductCategory> get(@RequestParam("id") Long id) {
        return success(dsProductCategoryMapper.selectById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建商品分类")
    public CommonResult<Long> create(@Valid @RequestBody DsProductCategory reqVO) {
        reqVO.setId(null);
        if (reqVO.getStatus() == null) {
            reqVO.setStatus(0);
        }
        dsProductCategoryMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品分类")
    public CommonResult<Boolean> update(@Valid @RequestBody DsProductCategory reqVO) {
        dsProductCategoryMapper.updateById(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品分类")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductCategoryMapper.deleteById(id);
        return success(true);
    }
}
