package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.shop.vo.DsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.controller.app.shop.vo.AppDsShopSaveReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsShop;
import cn.iocoder.yudao.module.ds.dal.mysql.DsShopMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_ALREADY_EXISTS;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.SHOP_NOT_EXISTS;

@Service
@Validated
public class DsShopServiceImpl implements DsShopService {

    @Resource
    private DsShopMapper dsShopMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createShop(Long uid, AppDsShopSaveReqVO reqVO) {
        DsShop existsShop = dsShopMapper.selectByUid(uid);
        if (existsShop != null) {
            throw exception(SHOP_ALREADY_EXISTS);
        }
        DsShop shop = new DsShop();
        fillShopFields(shop, reqVO);
        shop.setUid(uid);
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
    public DsShop validateShopById(Long shopId) {
        DsShop shop = dsShopMapper.selectById(shopId);
        if (shop == null) {
            throw exception(SHOP_NOT_EXISTS);
        }
        return shop;
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
