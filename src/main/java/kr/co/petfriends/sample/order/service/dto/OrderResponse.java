package kr.co.petfriends.sample.order.service.dto;

import java.time.LocalDateTime;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderResponse(
    String code,
    String userId,
    Integer quantity,
    String description,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
