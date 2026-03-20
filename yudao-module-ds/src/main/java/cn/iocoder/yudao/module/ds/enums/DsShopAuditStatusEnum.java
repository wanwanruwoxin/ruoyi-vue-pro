package cn.iocoder.yudao.module.ds.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DsShopAuditStatusEnum {

    PENDING(0),
    APPROVED(1),
    REJECTED(2);

    private final Integer status;
}
