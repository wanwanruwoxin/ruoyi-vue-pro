package cn.iocoder.yudao.module.ds.controller.app.product;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsMyProductPageReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductListReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductRespVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.product.vo.AppDsProductStatusReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsProduct;
import cn.iocoder.yudao.module.ds.service.DsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 商品")
@RestController
@RequestMapping("/ds/product")
@Validated
public class AppDsProductController {

    @Resource
    private DsProductService dsProductService;

    @PostMapping("/create")
    @Operation(summary = "创建商品")
    public CommonResult<Long> createProduct(@RequestBody @Valid AppDsProductSaveReqVO reqVO) {
        return success(dsProductService.createProduct(getLoginUserId(), reqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新商品")
    public CommonResult<Boolean> updateProduct(@RequestBody @Valid AppDsProductSaveReqVO reqVO) {
        dsProductService.updateProduct(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/status")
    @Operation(summary = "商品上下架")
    public CommonResult<Boolean> updateProductStatus(@RequestBody @Valid AppDsProductStatusReqVO reqVO) {
        dsProductService.updateProductStatus(getLoginUserId(), reqVO.getId(), reqVO.getSaleStatus());
        return success(true);
    }

    @PostMapping("/my-page")
    @Operation(summary = "获取我的商品分页")
    public CommonResult<PageResult<AppDsProductRespVO>> getMyProductPage(@RequestBody @Valid AppDsMyProductPageReqVO reqVO) {
        PageResult<DsProduct> pageResult = dsProductService.getMyProductPage(getLoginUserId(), reqVO);
        return success(convertPage(pageResult));
    }

    @PostMapping("/page")
    @Operation(summary = "获取店铺上架商品分页")
    public CommonResult<PageResult<AppDsProductRespVO>> getShelfProductPage(@RequestBody @Valid AppDsProductListReqVO reqVO) {
        PageResult<DsProduct> pageResult = dsProductService.getShelfProductPage(reqVO);
        return success(convertPage(pageResult));
    }

    private static PageResult<AppDsProductRespVO> convertPage(PageResult<DsProduct> pageResult) {
        PageResult<AppDsProductRespVO> result = new PageResult<>(pageResult.getTotal());
        result.setList(pageResult.getList().stream().map(AppDsProductController::convertProduct).toList());
        return result;
    }

    private static AppDsProductRespVO convertProduct(DsProduct product) {
        AppDsProductRespVO respVO = new AppDsProductRespVO();
        respVO.setId(product.getId());
        respVO.setShopId(product.getShopId());
        respVO.setProductName(product.getProductName());
        respVO.setPriceAmount(product.getPriceAmount());
        respVO.setStock(product.getStock());
        respVO.setDetailDesc(product.getDetailDesc());
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
