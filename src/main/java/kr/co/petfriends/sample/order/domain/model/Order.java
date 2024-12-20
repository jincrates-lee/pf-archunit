package kr.co.petfriends.sample.order.domain.model;

import kr.co.petfriends.sample.common.annotation.DomainModel;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import lombok.Builder;

@Builder
@DomainModel
public record Order(
    Long id,
    String code,
    String userId,
    OrderStatus status
) {

}
