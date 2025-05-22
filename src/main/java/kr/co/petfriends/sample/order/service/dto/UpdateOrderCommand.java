package kr.co.petfriends.sample.order.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateOrderCommand(
    @NotBlank(message = "사용자 ID는 필수입니다")
    String userId,
    
    @NotNull(message = "수량은 필수입니다")
    Integer quantity,
    
    String description
) {
}
