package kr.co.petfriends.sample.order.service.dto;

import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderResponse(
    String code,
    String userId,
    OrderStatus status
) {

}
