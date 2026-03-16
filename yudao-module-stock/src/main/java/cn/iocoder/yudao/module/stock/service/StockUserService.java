package cn.iocoder.yudao.module.stock.service;

import cn.iocoder.yudao.module.stock.controller.app.auth.vo.AppStockAuthLoginRespVO;

public interface StockUserService {

    Long register(String phone, String email, String password);

    AppStockAuthLoginRespVO login(String account, String password);

    AppStockAuthLoginRespVO refreshToken(String refreshToken);

    void logout(String accessToken);
}
