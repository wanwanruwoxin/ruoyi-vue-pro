package cn.iocoder.yudao.module.ds.controller.app.point;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ds.controller.app.point.vo.AppDsPointAccountRespVO;
import cn.iocoder.yudao.module.ds.controller.app.point.vo.AppDsPointGiftReqVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPointAccount;
import cn.iocoder.yudao.module.ds.service.DsPointAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - DS 积分")
@RestController
@RequestMapping("/ds/point")
@Validated
public class AppDsPointController {

    @Resource
    private DsPointAccountService dsPointAccountService;

    @PostMapping("/account")
    @Operation(summary = "获取我的积分账户")
    public CommonResult<AppDsPointAccountRespVO> getAccount() {
        DsPointAccount account = dsPointAccountService.getOrCreateAccount(getLoginUserId());
        AppDsPointAccountRespVO respVO = new AppDsPointAccountRespVO();
        respVO.setUid(account.getUid());
        respVO.setAvailablePoints(account.getAvailablePoints());
        respVO.setFrozenPoints(account.getFrozenPoints());
        respVO.setTotalEarnedPoints(account.getTotalEarnedPoints());
        respVO.setTotalSpentPoints(account.getTotalSpentPoints());
        return success(respVO);
    }

    @PostMapping("/gift")
    @Operation(summary = "赠送积分")
    public CommonResult<Boolean> giftPoints(@RequestBody @Valid AppDsPointGiftReqVO reqVO) {
        dsPointAccountService.giftPoints(getLoginUserId(), reqVO.getTargetMobile(), reqVO.getPoints());
        return success(true);
    }
}
