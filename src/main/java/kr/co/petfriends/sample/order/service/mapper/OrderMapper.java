package kr.co.petfriends.sample.order.service.mapper;

import static kr.co.petfriends.sample.common.constants.CommonConstant.ORDER_PREFIX;

import kr.co.petfriends.sample.common.utils.IdGenerator;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.model.Order;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;

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
