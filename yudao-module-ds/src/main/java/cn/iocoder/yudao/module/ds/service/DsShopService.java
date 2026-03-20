package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopAuditReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;

public interface DsShopService {

    Long createShop(Long uid, AppDsShopSaveReqVO reqVO);

    void updateShop(Long uid, AppDsShopSaveReqVO reqVO);

    Long createAdminShop(DsShopSaveReqVO reqVO);

    void updateAdminShop(DsShopSaveReqVO reqVO);

    void auditAdminShop(Long adminUserId, DsShopAuditReqVO reqVO);

    void deleteAdminShop(Long id);

    DsShop getAdminShop(Long id);

    PageResult<DsShop> getAdminShopPage(DsShopPageReqVO reqVO);

    DsShop getShopByUid(Long uid);

    DsShop getShopByBackendAdminUserId(Long backendAdminUserId);

    DsShop validateShopById(Long shopId);
}
