package cn.iocoder.yudao.module.ds.service;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopAuditReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUser;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserMapper;
import cn.iocoder.yudao.module.ds.enums.DsShopAuditStatusEnum;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.permission.RoleMapper;
import cn.iocoder.yudao.module.system.service.permission.PermissionService;
import cn.iocoder.yudao.module.system.service.user.AdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_AUDIT_NOT_ALLOWED;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_ALREADY_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_STATUS_ILLEGAL;

@Service
@Validated
public class DsShopServiceImpl implements DsShopService {

    private static final String MERCHANT_ROLE_CODE = "ds_merchant";

    @Resource
    private DsShopMapper dsShopMapper;
    @Resource
    private DsUserMapper dsUserMapper;
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createShop(Long uid, AppDsShopSaveReqVO reqVO) {
        DsShop existsShop = dsShopMapper.selectByUid(uid);
        if (existsShop != null) {
            if (!DsShopAuditStatusEnum.REJECTED.getStatus().equals(existsShop.getStatus())) {
                throw exception(SHOP_ALREADY_EXISTS);
            }
            fillShopFields(existsShop, reqVO);
            existsShop.setStatus(DsShopAuditStatusEnum.PENDING.getStatus());
            existsShop.setAuditRemark(null);
            existsShop.setAuditAdminUserId(null);
            dsShopMapper.updateById(existsShop);
            return existsShop.getId();
        }
        DsShop shop = new DsShop();
        fillShopFields(shop, reqVO);
        shop.setUid(uid);
        shop.setStatus(DsShopAuditStatusEnum.PENDING.getStatus());
        shop.setSort(0);
        dsShopMapper.insert(shop);
        return shop.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShop(Long uid, AppDsShopSaveReqVO reqVO) {
        DsShop shop = dsShopMapper.selectByUid(uid);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        fillShopFields(shop, reqVO);
        dsShopMapper.updateById(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAdminShop(DsShopSaveReqVO reqVO) {
        DsShop existsShop = dsShopMapper.selectByUid(reqVO.getUid());
        if (existsShop != null) {
            throw exception(SHOP_ALREADY_EXISTS);
        }
        DsShop shop = new DsShop();
        fillShopFields(shop, reqVO);
        shop.setUid(reqVO.getUid());
        shop.setStatus(DsShopAuditStatusEnum.APPROVED.getStatus());
        shop.setAuditRemark("后台创建");
        dsShopMapper.insert(shop);
        return shop.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminShop(DsShopSaveReqVO reqVO) {
        DsShop shop = validateShopById(reqVO.getId());
        fillShopFields(shop, reqVO);
        shop.setUid(reqVO.getUid());
        dsShopMapper.updateById(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditAdminShop(Long adminUserId, DsShopAuditReqVO reqVO) {
        DsShop shop = validateShopById(reqVO.getId());
        if (!DsShopAuditStatusEnum.PENDING.getStatus().equals(shop.getStatus())
                && !DsShopAuditStatusEnum.REJECTED.getStatus().equals(shop.getStatus())) {
            throw exception(SHOP_AUDIT_NOT_ALLOWED);
        }
        if (!DsShopAuditStatusEnum.APPROVED.getStatus().equals(reqVO.getStatus())
                && !DsShopAuditStatusEnum.REJECTED.getStatus().equals(reqVO.getStatus())) {
            throw exception(SHOP_STATUS_ILLEGAL);
        }
        shop.setStatus(reqVO.getStatus());
        shop.setAuditRemark(reqVO.getAuditRemark());
        shop.setAuditAdminUserId(adminUserId);
        if (DsShopAuditStatusEnum.APPROVED.getStatus().equals(reqVO.getStatus())) {
            Long backendAdminUserId = openBackendAdminAccount(shop);
            shop.setBackendAdminUserId(backendAdminUserId);
        }
        dsShopMapper.updateById(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdminShop(Long id) {
        validateShopById(id);
        dsShopMapper.deleteById(id);
    }

    @Override
    public DsShop getAdminShop(Long id) {
        return validateShopById(id);
    }

    @Override
    public PageResult<DsShop> getAdminShopPage(DsShopPageReqVO reqVO) {
        return dsShopMapper.selectPage(reqVO);
    }

    @Override
    public DsShop getShopByUid(Long uid) {
        return dsShopMapper.selectByUid(uid);
    }

    @Override
    public DsShop getShopByBackendAdminUserId(Long backendAdminUserId) {
        return dsShopMapper.selectByBackendAdminUserId(backendAdminUserId);
    }

    @Override
    public DsShop validateShopById(Long shopId) {
        DsShop shop = dsShopMapper.selectById(shopId);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        return shop;
    }

    private Long openBackendAdminAccount(DsShop shop) {
        if (shop.getBackendAdminUserId() != null) {
            AdminUserDO exists = adminUserService.getUser(shop.getBackendAdminUserId());
            if (exists != null) {
                assignMerchantRoleIfPresent(exists.getId());
                return exists.getId();
            }
        }
        DsUser dsUser = dsUserMapper.selectById(shop.getUid());
        if (dsUser == null || StrUtil.isBlank(dsUser.getMobile())) {
            throw exception(SHOP_NOT_EXISTS);
        }
        AdminUserDO adminUser = adminUserService.getUserByMobile(dsUser.getMobile());
        if (adminUser == null) {
            UserSaveReqVO userSaveReqVO = new UserSaveReqVO();
            userSaveReqVO.setUsername(buildUsername(dsUser));
            userSaveReqVO.setNickname(StrUtil.blankToDefault(dsUser.getNickname(), "商家" + dsUser.getId()));
            userSaveReqVO.setMobile(dsUser.getMobile());
            userSaveReqVO.setPassword(buildInitPassword(dsUser.getMobile()));
            Long adminUserId = adminUserService.createUser(userSaveReqVO);
            assignMerchantRoleIfPresent(adminUserId);
            return adminUserId;
        }
        assignMerchantRoleIfPresent(adminUser.getId());
        return adminUser.getId();
    }

    private void assignMerchantRoleIfPresent(Long adminUserId) {
        RoleDO merchantRole = roleMapper.selectByCode(MERCHANT_ROLE_CODE);
        if (merchantRole == null) {
            return;
        }
        Set<Long> roleIds = new HashSet<>(permissionService.getUserRoleIdListByUserId(adminUserId));
        roleIds.add(merchantRole.getId());
        permissionService.assignUserRole(adminUserId, roleIds);
    }

    private String buildUsername(DsUser dsUser) {
        String base = "ds" + dsUser.getMobile();
        if (base.length() > 30) {
            base = base.substring(0, 30);
        }
        if (adminUserService.getUserByUsername(base) == null) {
            return base;
        }
        String username = "ds" + dsUser.getId();
        if (username.length() < 4) {
            username = "dsm" + dsUser.getId();
        }
        return username.length() > 30 ? username.substring(0, 30) : username;
    }

    private String buildInitPassword(String mobile) {
        String suffix = mobile.length() >= 6 ? mobile.substring(mobile.length() - 6) : mobile;
        return "Ds" + suffix + "Aa1";
    }

    private static void fillShopFields(DsShop shop, AppDsShopSaveReqVO reqVO) {
        shop.setShopName(reqVO.getShopName());
        shop.setAvatarUrl(reqVO.getAvatarUrl());
        shop.setIntro(reqVO.getIntro());
        shop.setContactMobile(reqVO.getContactMobile());
        shop.setShipProvince(reqVO.getShipProvince());
        shop.setShipCity(reqVO.getShipCity());
        shop.setShipDistrict(reqVO.getShipDistrict());
        shop.setShipDetailAddress(reqVO.getShipDetailAddress());
    }

    private static void fillShopFields(DsShop shop, DsShopSaveReqVO reqVO) {
        shop.setShopName(reqVO.getShopName());
        shop.setAvatarUrl(reqVO.getAvatarUrl());
        shop.setIntro(reqVO.getIntro());
        shop.setContactMobile(reqVO.getContactMobile());
        shop.setShipProvince(reqVO.getShipProvince());
        shop.setShipCity(reqVO.getShipCity());
        shop.setShipDistrict(reqVO.getShipDistrict());
        shop.setShipDetailAddress(reqVO.getShipDetailAddress());
        shop.setSort(reqVO.getSort());
    }
}
