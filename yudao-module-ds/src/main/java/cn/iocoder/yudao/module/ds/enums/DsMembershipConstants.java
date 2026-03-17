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

    @Getter
    public enum PointChangeType {
        EARN("EARN"),
        SPEND("SPEND");

        private final String code;

        PointChangeType(String code) {
            this.code = code;
        }
    }

    @Getter
    public enum PointBizType {
        MEMBERSHIP_ORDER_PAY("MEMBERSHIP_ORDER_PAY"),
        SHOP_ORDER_PAY("SHOP_ORDER_PAY"),
        INVITE_MEMBERSHIP_REWARD("INVITE_MEMBERSHIP_REWARD");

        private final String code;

        PointBizType(String code) {
            this.code = code;
        }
    }

    @Getter
    public enum RewardTriggerEvent {
        MEMBERSHIP_ORDER_PAID_NORMAL("MEMBERSHIP_ORDER_PAID_NORMAL"),
        POINT_CONSUME_SCOPE("POINT_CONSUME_SCOPE");

        private final String code;

        RewardTriggerEvent(String code) {
            this.code = code;
        }
    }
}
