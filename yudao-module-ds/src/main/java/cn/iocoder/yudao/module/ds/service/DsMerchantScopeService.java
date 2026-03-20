package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

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

    @Data
    public static class DsMerchantScope {

        private final Long defaultUid;
        private final Long defaultShopId;
        private final Boolean canFilterUid;
        private final Boolean canFilterShopId;
    }
}
