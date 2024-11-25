package kr.co.petfriends.sample.domain.order.model;

import kr.co.petfriends.sample.domain.order.enums.OrderStatus;
import lombok.Builder;

@Builder
public record Order(
    Long id,
    String code,
    String userId,
    OrderStatus status
) {

}
