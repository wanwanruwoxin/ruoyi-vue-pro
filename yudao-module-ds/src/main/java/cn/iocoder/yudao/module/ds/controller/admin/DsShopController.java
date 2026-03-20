package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopRespVO;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopAuditReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopRespVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.service.DsShopService;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 电商店铺")
@RestController
@RequestMapping("/ds/shop")
@Validated
public class DsShopController {

    @Resource
    private DsShopService dsShopService;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private AdminUserService adminUserService;

    @PostMapping("/create")
    @Operation(summary = "创建店铺")
    @PreAuthorize("@ss.hasPermission('ds:shop:create')")
    public CommonResult<Long> createShop(@Valid @RequestBody DsShopSaveReqVO reqVO) {
        return success(dsShopService.createAdminShop(reqVO));
    }

    @PostMapping("/my")
    @Operation(summary = "获取我的店铺")
    public CommonResult<AppDsShopRespVO> getMyShop() {
        Long loginUserId = getLoginUserId();
        DsShop shop = dsShopService.getShopByBackendAdminUserId(loginUserId);
        return success(convertShop(shop));
    }

    @PostMapping("/my/create")
    @Operation(summary = "创建我的店铺")
    public CommonResult<Long> createMyShop(@Valid @RequestBody AppDsShopSaveReqVO reqVO) {
        return success(dsShopService.createShop(getLoginUserId(), reqVO));
    }

    @PostMapping("/my/update")
    @Operation(summary = "更新我的店铺")
    public CommonResult<Boolean> updateMyShop(@Valid @RequestBody AppDsShopSaveReqVO reqVO) {
        Long loginUserId = getLoginUserId();
        DsShop shop = dsShopService.getShopByBackendAdminUserId(loginUserId);
        if (shop != null) {
            dsShopService.updateShop(shop.getUid(), reqVO);
        }
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新店铺")
    @PreAuthorize("@ss.hasPermission('ds:shop:update')")
    public CommonResult<Boolean> updateShop(@Valid @RequestBody DsShopSaveReqVO reqVO) {
        dsShopService.updateAdminShop(reqVO);
        return success(true);
    }

    @PutMapping("/audit")
    @Operation(summary = "审核店铺")
    @PreAuthorize("@ss.hasPermission('ds:shop:audit')")
    public CommonResult<Boolean> auditShop(@Valid @RequestBody DsShopAuditReqVO reqVO) {
        dsShopService.auditAdminShop(getLoginUserId(), reqVO);
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
        DsShopRespVO respVO = BeanUtils.toBean(shop, DsShopRespVO.class);
        fillUserInfo(respVO);
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得店铺分页")
    public CommonResult<PageResult<DsShopRespVO>> getShopPage(@Valid DsShopPageReqVO reqVO) {
        PageResult<DsShop> pageResult = dsShopService.getAdminShopPage(reqVO);
        PageResult<DsShopRespVO> respPageResult = BeanUtils.toBean(pageResult, DsShopRespVO.class);
        fillUserInfo(respPageResult.getList());
        return success(respPageResult);
    }

    private void fillUserInfo(DsShopRespVO shop) {
        if (shop == null || shop.getUid() == null) {
            return;
        }
        DsUser user = dsUserMapper.selectById(shop.getUid());
        if (user != null) {
            shop.setUserNickname(user.getNickname());
            shop.setUserMobile(user.getMobile());
        }
        fillBackendAdminInfo(shop);
    }

    private void fillUserInfo(List<DsShopRespVO> shopList) {
        if (shopList == null || shopList.isEmpty()) {
            return;
        }
        Set<Long> userIds = shopList.stream().map(DsShopRespVO::getUid).collect(Collectors.toSet());
        List<DsUser> userList = dsUserMapper.selectList(DsUser::getId, userIds);
        Map<Long, DsUser> userMap = userList.stream().collect(Collectors.toMap(DsUser::getId, user -> user));
        Set<Long> backendAdminUserIds = shopList.stream()
                .map(DsShopRespVO::getBackendAdminUserId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, AdminUserDO> adminUserMap = adminUserService.getUserMap(backendAdminUserIds);
        shopList.forEach(shop -> {
            DsUser user = userMap.get(shop.getUid());
            if (user != null) {
                shop.setUserNickname(user.getNickname());
                shop.setUserMobile(user.getMobile());
            }
            AdminUserDO adminUser = adminUserMap.get(shop.getBackendAdminUserId());
            if (adminUser != null) {
                shop.setBackendAdminUsername(adminUser.getUsername());
            }
        });
    }

    private void fillBackendAdminInfo(DsShopRespVO shop) {
        if (shop == null || shop.getBackendAdminUserId() == null) {
            return;
        }
        AdminUserDO adminUser = adminUserService.getUser(shop.getBackendAdminUserId());
        if (adminUser == null) {
            return;
        }
        shop.setBackendAdminUsername(adminUser.getUsername());
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
