package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import cn.iocoder.yudao.module.ds.dal.mysql.DsProductSkuMapper;
import cn.iocoder.yudao.module.ds.service.DsProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商商品SKU")
@RestController
@RequestMapping("/ds/product-sku")
@Validated
public class DsProductSkuController {

    @Resource
    private DsProductSkuService dsProductSkuService;
    @Resource
    private DsProductSkuMapper dsProductSkuMapper;

    @GetMapping("/page")
    @Operation(summary = "SKU 分页")
    public CommonResult<PageResult<DsProductSku>> getPage(PageParam pageParam) {
        return success(dsProductSkuMapper.selectPage(pageParam, new LambdaQueryWrapperX<DsProductSku>()
                .orderByDesc(DsProductSku::getId)));
    }

    @PostMapping("/create")
    @Operation(summary = "创建 SKU")
    public CommonResult<Long> create(@Valid @RequestBody DsProductSku reqVO) {
        reqVO.setId(null);
        dsProductSkuMapper.insert(reqVO);
        return success(reqVO.getId());
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 SKU")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductSkuMapper.deleteById(id);
        return success(true);
    }
}
