package cn.iocoder.yudao.module.ds.controller.app.address;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressCreateReqVO;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressIdReqVO;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressRespVO;
import cn.iocoder.yudao.module.ds.controller.app.address.vo.AppDsUserAddressUpdateReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsUserAddress;
import cn.iocoder.yudao.module.ds.service.DsUserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 收货地址")
@RestController
@RequestMapping("/ds/address")
@Validated
public class AppDsUserAddressController {

    @Resource
    private DsUserAddressService dsUserAddressService;

    @PostMapping("/list")
    @Operation(summary = "获取我的收货地址列表")
    public CommonResult<List<AppDsUserAddressRespVO>> list() {
        List<DsUserAddress> addresses = dsUserAddressService.getAddressList(getLoginUserId());
        return success(addresses.stream().map(this::convert).toList());
    }

    @PostMapping("/default")
    @Operation(summary = "获取默认收货地址")
    public CommonResult<AppDsUserAddressRespVO> getDefaultAddress() {
        DsUserAddress address = dsUserAddressService.getDefaultAddress(getLoginUserId());
        return success(address == null ? null : convert(address));
    }

    @PostMapping("/create")
    @Operation(summary = "新增收货地址")
    public CommonResult<Long> create(@RequestBody @Valid AppDsUserAddressCreateReqVO reqVO) {
        return success(dsUserAddressService.createAddress(getLoginUserId(), reqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新收货地址")
    public CommonResult<Boolean> update(@RequestBody @Valid AppDsUserAddressUpdateReqVO reqVO) {
        dsUserAddressService.updateAddress(getLoginUserId(), reqVO);
        return success(true);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除收货地址")
    public CommonResult<Boolean> delete(@RequestBody @Valid AppDsUserAddressIdReqVO reqVO) {
        dsUserAddressService.deleteAddress(getLoginUserId(), reqVO.getId());
        return success(true);
    }

    @PostMapping("/set-default")
    @Operation(summary = "设置默认收货地址")
    public CommonResult<Boolean> setDefault(@RequestBody @Valid AppDsUserAddressIdReqVO reqVO) {
        dsUserAddressService.setDefaultAddress(getLoginUserId(), reqVO.getId());
        return success(true);
    }

    private AppDsUserAddressRespVO convert(DsUserAddress address) {
        AppDsUserAddressRespVO respVO = new AppDsUserAddressRespVO();
        respVO.setId(address.getId());
        respVO.setReceiverName(address.getReceiverName());
        respVO.setReceiverMobile(address.getReceiverMobile());
        respVO.setProvince(address.getProvince());
        respVO.setCity(address.getCity());
        respVO.setDistrict(address.getDistrict());
        respVO.setDetailAddress(address.getDetailAddress());
        respVO.setIsDefault(address.getIsDefault());
        return respVO;
    }
}
