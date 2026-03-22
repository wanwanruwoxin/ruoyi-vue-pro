package cn.iocoder.yudao.module.ds.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_060_000_000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_060_000_001, "登录失败，账号被禁用");
    ErrorCode USER_MOBILE_EXISTS = new ErrorCode(1_060_000_002, "手机号已被注册");
    ErrorCode INVITER_NOT_EXISTS = new ErrorCode(1_060_000_003, "邀请人不存在");
    ErrorCode INVITE_BIND_SELF = new ErrorCode(1_060_000_004, "不能绑定自己为邀请人");
    ErrorCode INVITE_RELATION_EXISTS = new ErrorCode(1_060_000_005, "当前账号已绑定邀请人");
    ErrorCode INVITE_BIND_LOOP = new ErrorCode(1_060_000_006, "邀请关系绑定不合法");
    ErrorCode MEMBERSHIP_PLAN_NOT_EXISTS = new ErrorCode(1_060_000_007, "会员档位不存在");
    ErrorCode MEMBERSHIP_ORDER_NOT_EXISTS = new ErrorCode(1_060_000_008, "会员订单不存在");
    ErrorCode MEMBERSHIP_ORDER_STATUS_ILLEGAL = new ErrorCode(1_060_000_009, "会员订单状态不合法");
    ErrorCode POINT_ACCOUNT_INSUFFICIENT = new ErrorCode(1_060_000_010, "积分余额不足");
    ErrorCode POINT_CONSUME_SCOPE_DISABLED = new ErrorCode(1_060_000_011, "当前场景不支持积分消费");
    ErrorCode POINT_GIFT_TARGET_NOT_EXISTS = new ErrorCode(1_060_000_012, "赠送对象不存在");
    ErrorCode POINT_GIFT_SELF_NOT_ALLOWED = new ErrorCode(1_060_000_013, "不能给自己赠送积分");
    ErrorCode SHOP_ALREADY_EXISTS = new ErrorCode(1_060_000_014, "当前用户已创建店铺");
    ErrorCode SHOP_NOT_EXISTS = new ErrorCode(1_060_000_015, "店铺不存在");
    ErrorCode PRODUCT_NOT_EXISTS = new ErrorCode(1_060_000_016, "商品不存在");
    ErrorCode PRODUCT_ACCESS_DENIED = new ErrorCode(1_060_000_017, "无权操作该商品");
    ErrorCode PRODUCT_STATUS_ILLEGAL = new ErrorCode(1_060_000_018, "商品状态不合法");
    ErrorCode PRODUCT_SKU_NOT_EXISTS = new ErrorCode(1_060_000_019, "商品 SKU 不存在");
    ErrorCode SHOP_STATUS_ILLEGAL = new ErrorCode(1_060_000_020, "店铺状态不合法");
    ErrorCode SHOP_AUDIT_NOT_ALLOWED = new ErrorCode(1_060_000_021, "当前店铺状态不允许审核");
    ErrorCode SHOP_NOT_APPROVED = new ErrorCode(1_060_000_022, "店铺未审核通过，无法操作");
    ErrorCode AUTH_ADMIN_NOT_OPENED = new ErrorCode(1_060_000_023, "店铺审核通过后才可登录商家后台");
    ErrorCode USER_ADDRESS_NOT_EXISTS = new ErrorCode(1_060_000_024, "收货地址不存在");
    ErrorCode SHOP_ORDER_NOT_EXISTS = new ErrorCode(1_060_000_025, "订单不存在");
    ErrorCode PRODUCT_STOCK_NOT_ENOUGH = new ErrorCode(1_060_000_026, "商品库存不足");
}
