package kr.co.petfriends.sample.order.service.mapper;

import static kr.co.petfriends.sample.common.constants.CommonConstant.ORDER_PREFIX;

import java.time.LocalDateTime;
import kr.co.petfriends.sample.common.utils.IdGenerator;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.model.Order;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;

public class OrderMapper {

    public static Order toDomain(CreateOrderCommand command) {
        LocalDateTime now = LocalDateTime.now();
        return Order.builder()
            .code(IdGenerator.generateId(ORDER_PREFIX))
            .userId(command.userId())
            .quantity(command.quantity())
            .description(command.description())
            .status(OrderStatus.REQUESTED)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public static OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
            .code(order.code())
            .userId(order.userId())
            .quantity(order.quantity())
            .description(order.description())
            .status(order.status())
            .createdAt(order.createdAt())
            .updatedAt(order.updatedAt())
            .build();
    }
}
