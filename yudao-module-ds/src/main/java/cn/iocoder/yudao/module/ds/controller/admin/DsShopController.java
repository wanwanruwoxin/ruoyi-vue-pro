package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.service.DsShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 电商店铺")
@RestController
@RequestMapping("/ds/shop")
@Validated
public class DsShopController {

    @Resource
    private DsShopService dsShopService;

    @PostMapping("/create")
    @Operation(summary = "创建店铺")
    @PreAuthorize("@ss.hasPermission('ds:shop:create')")
    public CommonResult<Long> createShop(@Valid @RequestBody DsShopSaveReqVO reqVO) {
        return success(dsShopService.createAdminShop(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新店铺")
    @PreAuthorize("@ss.hasPermission('ds:shop:update')")
    public CommonResult<Boolean> updateShop(@Valid @RequestBody DsShopSaveReqVO reqVO) {
        dsShopService.updateAdminShop(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除店铺")
    @Parameter(name = "id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('ds:shop:delete')")
    public CommonResult<Boolean> deleteShop(@RequestParam("id") Long id) {
        dsShopService.deleteAdminShop(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得店铺详情")
    @Parameter(name = "id", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('ds:shop:query')")
    public CommonResult<DsShopRespVO> getShop(@RequestParam("id") Long id) {
        DsShop shop = dsShopService.getAdminShop(id);
        return success(BeanUtils.toBean(shop, DsShopRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得店铺分页")
    @PreAuthorize("@ss.hasPermission('ds:shop:query')")
    public CommonResult<PageResult<DsShopRespVO>> getShopPage(@Valid DsShopPageReqVO reqVO) {
        PageResult<DsShop> pageResult = dsShopService.getAdminShopPage(reqVO);
        return success(BeanUtils.toBean(pageResult, DsShopRespVO.class));
    }
}
