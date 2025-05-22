package kr.co.petfriends.sample.order.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderCommand(
    @NotBlank(message = "주문자 아이디는 필수입니다.")
    String userId,
    
    @NotNull(message = "수량은 필수입니다.")
    @Positive(message = "수량은 양수여야 합니다.")
    Integer quantity,
    
    String description
) {
}
