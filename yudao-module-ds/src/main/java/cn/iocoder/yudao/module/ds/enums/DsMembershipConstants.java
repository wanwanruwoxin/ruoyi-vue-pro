package cn.iocoder.yudao.module.ds.enums;

import lombok.Getter;

public final class DsMembershipConstants {

    private DsMembershipConstants() {
    }

    @Getter
    public enum PlanCode {
        NORMAL("NORMAL"),
        ADVANCED("ADVANCED");

        private final String code;

        PlanCode(String code) {
            this.code = code;
        }

    }

    @Getter
    public enum MemberStatus {
        UNOPENED("UNOPENED"),
        ACTIVE("ACTIVE"),
        EXPIRED("EXPIRED");

        private final String code;

        MemberStatus(String code) {
            this.code = code;
        }

    }

    @Getter
    public enum PayStatus {
        PENDING("PENDING"),
        PAID("PAID"),
        CLOSED("CLOSED"),
        REFUNDED("REFUNDED");

        private final String code;

        PayStatus(String code) {
            this.code = code;
        }

    }

    @Getter
    public enum RefundStatus {
        NONE("NONE"),
        SUCCESS("SUCCESS");

        private final String code;

        RefundStatus(String code) {
            this.code = code;
        }

    }
}
