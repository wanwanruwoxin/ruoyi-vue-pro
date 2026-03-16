package cn.iocoder.yudao.module.ds.service;

import cn.iocoder.yudao.module.ds.controller.app.auth.vo.AppDsAuthLoginRespVO;

public interface DsUserService {

    Long register(String mobile, String nickname, String avatar, String registerChannel);

    AppDsAuthLoginRespVO login(String mobile);

    AppDsAuthLoginRespVO refreshToken(String refreshToken);

    void logout(String accessToken);
}
