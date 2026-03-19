package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import cn.iocoder.yudao.module.ds.service.DsProductSkuService;
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

@Tag(name = "管理后台 - 电商商品SKU")
@RestController
@RequestMapping("/ds/product-sku")
@Validated
public class DsProductSkuController {

    @Resource
    private DsProductSkuService dsProductSkuService;

    @GetMapping("/page")
    @Operation(summary = "SKU 分页")
    public CommonResult<PageResult<DsProductSkuRespVO>> getPage(@Valid DsProductSkuPageReqVO reqVO) {
        PageResult<DsProductSku> pageResult = dsProductSkuService.getProductSkuPage(reqVO);
        return success(BeanUtils.toBean(pageResult, DsProductSkuRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "SKU 详情")
    @Parameter(name = "id", required = true)
    public CommonResult<DsProductSkuRespVO> get(@RequestParam("id") Long id) {
        DsProductSku sku = dsProductSkuService.getProductSku(id);
        return success(BeanUtils.toBean(sku, DsProductSkuRespVO.class));
    }

    @PostMapping("/create")
    @Operation(summary = "创建 SKU")
    public CommonResult<Long> create(@Valid @RequestBody DsProductSkuSaveReqVO reqVO) {
        return success(dsProductSkuService.createProductSku(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 SKU")
    public CommonResult<Boolean> update(@Valid @RequestBody DsProductSkuSaveReqVO reqVO) {
        dsProductSkuService.updateProductSku(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 SKU")
    @Parameter(name = "id", required = true)
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        dsProductSkuService.deleteProductSku(id);
        return success(true);
    }
}
