package cn.iocoder.yudao.module.ds.controller.app.shop;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopRespVO;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.service.DsShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 店铺")
@RestController
@RequestMapping("/ds/shop")
@Validated
public class AppDsShopController {

    @Resource
    private DsShopService dsShopService;

    @PostMapping("/my")
    @Operation(summary = "获取我的店铺")
    public CommonResult<AppDsShopRespVO> getMyShop() {
        Long loginUserId = getLoginUserId();
        DsShop shop = dsShopService.getShopByUid(loginUserId);
        return success(convertShop(shop));
    }

    @PostMapping("/create")
    @Operation(summary = "创建店铺")
    public CommonResult<Long> createShop(@RequestBody @Valid AppDsShopSaveReqVO reqVO) {
        return success(dsShopService.createShop(getLoginUserId(), reqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新店铺")
    public CommonResult<Boolean> updateShop(@RequestBody @Valid AppDsShopSaveReqVO reqVO) {
        Long loginUserId = getLoginUserId();
        dsShopService.updateShop(loginUserId, reqVO);
        return success(true);
    }

    private static AppDsShopRespVO convertShop(DsShop shop) {
        if (shop == null) {
            return null;
        }
        AppDsShopRespVO respVO = new AppDsShopRespVO();
        respVO.setId(shop.getId());
        respVO.setUid(shop.getUid());
        respVO.setShopName(shop.getShopName());
        respVO.setAvatarUrl(shop.getAvatarUrl());
        respVO.setIntro(shop.getIntro());
        respVO.setContactMobile(shop.getContactMobile());
        respVO.setShipProvince(shop.getShipProvince());
        respVO.setShipCity(shop.getShipCity());
        respVO.setShipDistrict(shop.getShipDistrict());
        respVO.setShipDetailAddress(shop.getShipDetailAddress());
        respVO.setStatus(shop.getStatus());
        respVO.setAuditRemark(shop.getAuditRemark());
        return respVO;
    }
}
