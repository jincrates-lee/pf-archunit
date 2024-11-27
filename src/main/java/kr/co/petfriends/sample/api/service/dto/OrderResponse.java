package kr.co.petfriends.sample.api.service.dto;

import kr.co.petfriends.sample.domain.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderResponse(
    String code,
    String userId,
    OrderStatus status
) {

}
