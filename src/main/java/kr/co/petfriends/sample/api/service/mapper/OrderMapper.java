package kr.co.petfriends.sample.api.service.mapper;

import static kr.co.petfriends.sample.common.constants.CommonConstant.ORDER_PREFIX;

import kr.co.petfriends.sample.api.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.api.service.dto.OrderResponse;
import kr.co.petfriends.sample.common.utils.IdGenerator;
import kr.co.petfriends.sample.domain.enums.OrderStatus;
import kr.co.petfriends.sample.domain.model.Order;

public class OrderMapper {

    public static Order toDomain(CreateOrderCommand command) {
        return Order.builder()
            .code(IdGenerator.generateId(ORDER_PREFIX))
            .userId(command.userId())
            .status(OrderStatus.REQUESTED)
            .build();
    }

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
            .code(order.code())
            .userId(order.userId())
            .status(order.status())
            .build();
    }
}
