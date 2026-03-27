package cn.iocoder.yudao.module.ds.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo.DsPlatformAccountPageReqVO;
import cn.iocoder.yudao.module.ds.controller.admin.platformaccount.vo.DsPlatformAccountRespVO;
import cn.iocoder.yudao.module.ds.dal.dataobject.DsPlatformAccount;
import cn.iocoder.yudao.module.ds.dal.mysql.DsPlatformAccountMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - DS 平台账户")
@RestController
@RequestMapping("/ds/platform-account")
@Validated
public class DsPlatformAccountController {

    @Resource
    private DsPlatformAccountMapper dsPlatformAccountMapper;

    @GetMapping("/page")
    @Operation(summary = "平台账户分页")
    public CommonResult<PageResult<DsPlatformAccountRespVO>> getPlatformAccountPage(@Validated DsPlatformAccountPageReqVO reqVO) {
        PageResult<DsPlatformAccount> pageResult = dsPlatformAccountMapper.selectPage(reqVO, new LambdaQueryWrapperX<DsPlatformAccount>()
                .likeIfPresent(DsPlatformAccount::getAccountCode, reqVO.getAccountCode())
                .orderByDesc(DsPlatformAccount::getId));
        List<DsPlatformAccountRespVO> list = BeanUtils.toBean(pageResult.getList(), DsPlatformAccountRespVO.class);
        return success(new PageResult<>(list, pageResult.getTotal()));
    }
}
