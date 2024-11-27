package kr.co.petfriends.sample.domain.enums;

public enum OrderStatus {
    REQUESTED("주문 접수"),
    COMPLETED("주문 완료"),
    ;
    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
