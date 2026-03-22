package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressCreateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressUpdateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.dal.mysql.DsUserAddressMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ds.enums.ErrorCodeConstants.USER_ADDRESS_NOT_EXISTS;

@Service
@Validated
public class DsUserAddressServiceImpl implements DsUserAddressService {

    @Resource
    private DsUserAddressMapper dsUserAddressMapper;

    @Override
    public List<DsUserAddress> getAddressList(Long uid) {
        return dsUserAddressMapper.selectListByUid(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAddress(Long uid, AppDsUserAddressCreateReqVO reqVO) {
        DsUserAddress address = new DsUserAddress();
        fillAddress(address, reqVO);
        address.setUid(uid);
        List<DsUserAddress> addresses = dsUserAddressMapper.selectListByUid(uid);
        boolean shouldDefault = Boolean.TRUE.equals(reqVO.getDefaultAddress()) || addresses.isEmpty();
        address.setIsDefault(shouldDefault ? 1 : 0);
        dsUserAddressMapper.insert(address);
        if (shouldDefault) {
            setDefaultAddress(uid, address.getId());
        }
        return address.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long uid, AppDsUserAddressUpdateReqVO reqVO) {
        DsUserAddress address = validateAddress(uid, reqVO.getId());
        fillAddress(address, reqVO);
        boolean shouldDefault = Boolean.TRUE.equals(reqVO.getDefaultAddress());
        if (shouldDefault) {
            address.setIsDefault(1);
        } else if (Integer.valueOf(1).equals(address.getIsDefault())) {
            address.setIsDefault(0);
        }
        dsUserAddressMapper.updateById(address);
        if (shouldDefault) {
            setDefaultAddress(uid, address.getId());
            return;
        }
        ensureHasDefaultAddress(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long uid, Long id) {
        DsUserAddress address = validateAddress(uid, id);
        dsUserAddressMapper.deleteById(address.getId());
        if (Integer.valueOf(1).equals(address.getIsDefault())) {
            ensureHasDefaultAddress(uid);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long uid, Long id) {
        DsUserAddress target = validateAddress(uid, id);
        List<DsUserAddress> addresses = dsUserAddressMapper.selectListByUid(uid);
        for (DsUserAddress address : addresses) {
            int isDefault = address.getId().equals(target.getId()) ? 1 : 0;
            if (!Integer.valueOf(isDefault).equals(address.getIsDefault())) {
                address.setIsDefault(isDefault);
                dsUserAddressMapper.updateById(address);
            }
        }
    }

    @Override
    public DsUserAddress getDefaultAddress(Long uid) {
        DsUserAddress defaultAddress = dsUserAddressMapper.selectDefaultByUid(uid);
        if (defaultAddress != null) {
            return defaultAddress;
        }
        List<DsUserAddress> addresses = dsUserAddressMapper.selectListByUid(uid);
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    private void ensureHasDefaultAddress(Long uid) {
        DsUserAddress defaultAddress = dsUserAddressMapper.selectDefaultByUid(uid);
        if (defaultAddress != null) {
            return;
        }
        List<DsUserAddress> addresses = dsUserAddressMapper.selectListByUid(uid);
        if (addresses.isEmpty()) {
            return;
        }
        DsUserAddress first = addresses.get(0);
        first.setIsDefault(1);
        dsUserAddressMapper.updateById(first);
    }

    private DsUserAddress validateAddress(Long uid, Long id) {
        DsUserAddress address = dsUserAddressMapper.selectByIdAndUid(id, uid);
        if (address == null) {
            throw exception(USER_ADDRESS_NOT_EXISTS);
        }
        return address;
    }

    private static void fillAddress(DsUserAddress address, AppDsUserAddressCreateReqVO reqVO) {
        address.setReceiverName(reqVO.getReceiverName());
        address.setReceiverMobile(reqVO.getReceiverMobile());
        address.setProvince(reqVO.getProvince());
        address.setCity(reqVO.getCity());
        address.setDistrict(reqVO.getDistrict());
        address.setDetailAddress(reqVO.getDetailAddress());
    }
}
