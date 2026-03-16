package cn.iocoder.yudao.module.ds.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_060_000_000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_060_000_001, "登录失败，账号被禁用");
    ErrorCode USER_MOBILE_EXISTS = new ErrorCode(1_060_000_002, "手机号已被注册");
}
