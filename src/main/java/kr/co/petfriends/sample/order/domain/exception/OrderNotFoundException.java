package kr.co.petfriends.sample.order.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String orderCode) {
        super("주문을 찾을 수 없습니다: " + orderCode);
    }
    
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
