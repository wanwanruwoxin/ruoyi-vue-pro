package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;
import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthRegisterReqVO;

public interface DsUserService {

//    void sendRegisterSmsCode(String mobile);

    Long register(AppDsAuthRegisterReqVO reqVO);

    AppDsAuthLoginRespVO login(String mobile, String password);

    AppDsAuthLoginRespVO refreshToken(String refreshToken);

    void logout(String accessToken);
}
