package cn.iocoder.yudao.module.ds.controller.app.product;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductCategory;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductCategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - DS 商品分类")
@RestController
@RequestMapping("/ds/product-category")
@Validated
public class AppDsProductCategoryController {

    @Resource
    private DsProductCategoryMapper dsProductCategoryMapper;

    @GetMapping("/list")
    @Operation(summary = "获取商品分类列表")
    public CommonResult<List<DsProductCategory>> getCategoryList(@RequestParam(value = "status", required = false) Integer status) {
        List<DsProductCategory> dsProductCategories = dsProductCategoryMapper.selectList(new LambdaQueryWrapperX<DsProductCategory>()
                .eqIfPresent(DsProductCategory::getStatus, status)
                .orderByAsc(DsProductCategory::getSort)
                .orderByDesc(DsProductCategory::getId));
        return success(dsProductCategories);
    } 
}
