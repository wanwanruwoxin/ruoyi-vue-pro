package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;

public interface DsShopService {

    Long createShop(Long uid, AppDsShopSaveReqVO reqVO);

    void updateShop(Long uid, AppDsShopSaveReqVO reqVO);

    DsShop getShopByUid(Long uid);

    DsShop validateShopById(Long shopId);
}
