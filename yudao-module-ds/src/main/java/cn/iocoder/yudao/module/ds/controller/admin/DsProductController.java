package cn.iocoder.yudao.module.ds.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductRespVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductStatusReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSkuRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.product.vo.DsProductUpdateStatusReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProductSku;
import cn.iocoder.yudao.module.ds.service.DsProductService;
import cn.iocoder.yudao.module.ds.service.DsProductSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.io.IOException;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 电商商品")
@RestController
@RequestMapping("/ds/product")
@Validated
public class DsProductController {

    @Resource
    private DsProductService dsProductService;
    @Resource
    private DsProductSkuService dsProductSkuService;

    @PostMapping("/create")
    @Operation(summary = "创建商品")
    @PreAuthorize("@ss.hasPermission('ds:product:create')")
    public CommonResult<Long> createProduct(@Valid @RequestBody DsProductSaveReqVO reqVO) {
        return success(dsProductService.createAdminProduct(reqVO));
    }

    @PostMapping("/my/create")
    @Operation(summary = "创建我的商品")
    public CommonResult<Long> createMyProduct(@Valid @RequestBody AppDsProductSaveReqVO reqVO) {
        return success(dsProductService.createProduct(getLoginUserId(), reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新商品")
    @PreAuthorize("@ss.hasPermission('ds:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody DsProductSaveReqVO reqVO) {
        dsProductService.updateAdminProduct(reqVO);
        return success(true);
    }

    @PostMapping("/my/update")
    @Operation(summary = "更新我的商品")
    public CommonResult<Boolean> updateMyProduct(@Valid @RequestBody AppDsProductSaveReqVO reqVO) {
        dsProductService.updateProduct(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/my/status")
    @Operation(summary = "更新我的商品上下架状态")
    public CommonResult<Boolean> updateMyProductStatus(@Valid @RequestBody AppDsProductStatusReqVO reqVO) {
        dsProductService.updateProductStatus(getLoginUserId(), reqVO.getId(), reqVO.getSaleStatus());
        return success(true);
    }

    @PostMapping("/my-page")
    @Operation(summary = "获取我的商品分页")
    public CommonResult<PageResult<AppDsProductRespVO>> getMyProductPage(@Valid @RequestBody AppDsMyProductPageReqVO reqVO) {
        PageResult<DsProduct> pageResult = dsProductService.getMyProductPage(getLoginUserId(), reqVO);
        return success(convertPage(pageResult));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除商品")
    @Parameter(name = "id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('ds:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") Long id) {
        dsProductService.deleteAdminProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得商品详情")
    @Parameter(name = "id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('ds:product:query')")
    public CommonResult<DsProductRespVO> getProduct(@RequestParam("id") Long id) {
        DsProduct product = dsProductService.getAdminProduct(id);
        List<DsProductSku> skus = dsProductSkuService.getProductSkuListBySpuId(id);
        DsProductRespVO respVO = BeanUtils.toBean(product, DsProductRespVO.class);
        respVO.setSkus(BeanUtils.toBean(skus, DsProductSkuRespVO.class));
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得商品分页")
    @PreAuthorize("@ss.hasPermission('ds:product:query')")
    public CommonResult<PageResult<DsProductRespVO>> getProductPage(@Valid DsProductPageReqVO reqVO) {
        PageResult<DsProduct> pageResult = dsProductService.getAdminProductPage(reqVO);
        return success(BeanUtils.toBean(pageResult, DsProductRespVO.class));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新商品状态")
    @PreAuthorize("@ss.hasPermission('ds:product:update')")
    public CommonResult<Boolean> updateProductStatus(@Valid @RequestBody DsProductUpdateStatusReqVO reqVO) {
        dsProductService.updateAdminProductStatus(reqVO.getId(), reqVO.getSaleStatus());
        return success(true);
    }

    @GetMapping("/get-count")
    @Operation(summary = "获得商品分页 tab count")
    @PreAuthorize("@ss.hasPermission('ds:product:query')")
    public CommonResult<Map<Integer, Long>> getProductCount() {
        return success(dsProductService.getTabsCount());
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出商品")
    @PreAuthorize("@ss.hasPermission('ds:product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductList(@Validated DsProductPageReqVO reqVO,
                                  HttpServletResponse response) throws IOException {
        reqVO.setPageSize(PAGE_SIZE_NONE);
        List<DsProduct> list = dsProductService.getAdminProductPage(reqVO).getList();
        ExcelUtils.write(response, "商品列表.xls", "数据", DsProductRespVO.class,
                BeanUtils.toBean(list, DsProductRespVO.class));
    }

    private static PageResult<AppDsProductRespVO> convertPage(PageResult<DsProduct> pageResult) {
        PageResult<AppDsProductRespVO> result = new PageResult<>(pageResult.getTotal());
        result.setList(pageResult.getList().stream().map(DsProductController::convertProduct).toList());
        return result;
    }

    private static AppDsProductRespVO convertProduct(DsProduct product) {
        AppDsProductRespVO respVO = new AppDsProductRespVO();
        respVO.setId(product.getId());
        respVO.setShopId(product.getShopId());
        respVO.setProductName(product.getProductName());
        respVO.setPriceAmount(product.getPriceAmount());
        respVO.setStock(product.getStock());
        respVO.setDetailDesc(StrUtil.emptyToDefault(product.getDescription(), product.getDetailDesc()));
        respVO.setSaleStatus(product.getSaleStatus());
        respVO.setSort(product.getSort());
        respVO.setImageUrls(splitUrls(product.getImageUrls()));
        respVO.setVideoUrls(splitUrls(product.getVideoUrls()));
        return respVO;
    }

    private static List<String> splitUrls(String urls) {
        if (StrUtil.isBlank(urls)) {
            return Collections.emptyList();
        }
        return Arrays.stream(urls.split(","))
                .map(String::trim)
                .filter(StrUtil::isNotBlank)
                .toList();
    }
}
