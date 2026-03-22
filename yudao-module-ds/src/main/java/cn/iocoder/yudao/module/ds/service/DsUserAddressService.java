package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressCreateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressUpdateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;

import java.util.List;

public interface DsUserAddressService {

    List<DsUserAddress> getAddressList(Long uid);

    Long createAddress(Long uid, AppDsUserAddressCreateReqVO reqVO);

    void updateAddress(Long uid, AppDsUserAddressUpdateReqVO reqVO);

    void deleteAddress(Long uid, Long id);

    void setDefaultAddress(Long uid, Long id);

    DsUserAddress getDefaultAddress(Long uid);
}
