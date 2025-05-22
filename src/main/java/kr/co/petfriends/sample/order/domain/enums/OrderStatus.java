package kr.co.petfriends.sample.order.domain.enums;

public enum OrderStatus {
    REQUESTED("주문 접수"),
    CONFIRMED("주문 확정"),
    PROCESSING("처리 중"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    COMPLETED("주문 완료"),
    CANCELLED("주문 취소"),
    REFUNDED("환불 완료");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case REQUESTED -> newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == SHIPPED || newStatus == CANCELLED;
            case SHIPPED -> newStatus == DELIVERED;
            case DELIVERED -> newStatus == COMPLETED || newStatus == REFUNDED;
            case COMPLETED, CANCELLED, REFUNDED -> false;
        };
    }

    public boolean isCancellable() {
        return this == REQUESTED || this == CONFIRMED || this == PROCESSING;
    }

    public boolean isConfirmable() {
        return this == REQUESTED;
    }
}
