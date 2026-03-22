package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;

@Service
public class DsMerchantScopeService {

    @Resource
    private DsShopService dsShopService;

    public DsMerchantScope getCurrentScope() {
        Long loginUserId = getLoginUserId();
        if (loginUserId == null) {
            return new DsMerchantScope(null, null, true, true);
        }
        DsShop shop = dsShopService.getShopByBackendAdminUserId(loginUserId);
        if (shop == null) {
            return new DsMerchantScope(null, null, true, true);
        }
        return new DsMerchantScope(shop.getUid(), shop.getId(), false, false);
    }

    public Long getCurrentDsUid() {
        Long backendAdminUserId = getLoginUserId();
        DsShop shop = dsShopService.getShopByBackendAdminUserId(backendAdminUserId);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        return shop.getUid();
    }

    @Data
    public static class DsMerchantScope {

        private final Long defaultUid;
        private final Long defaultShopId;
        private final Boolean canFilterUid;
        private final Boolean canFilterShopId;
    }
}
