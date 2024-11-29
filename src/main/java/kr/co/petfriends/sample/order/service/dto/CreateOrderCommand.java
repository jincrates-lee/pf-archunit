package kr.co.petfriends.sample.order.service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderCommand(
    @NotBlank(message = "주문자 아이디는 필수입니다.")
    String userId
) {

}
