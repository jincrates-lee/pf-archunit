package kr.co.petfriends.sample.infrastructure.dataaccess.mapper;

import kr.co.petfriends.sample.domain.model.Order;
import kr.co.petfriends.sample.infrastructure.dataaccess.entity.OrderEntity;

public class OrderDataMapper {

    public static OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
            .id(order.id())
            .code(order.code())
            .userId(order.userId())
            .status(order.status())
            .build();
    }

    public static Order toDomain(OrderEntity orderEntity) {
        return Order.builder()
            .id(orderEntity.getId())
            .code(orderEntity.getCode())
            .userId(orderEntity.getUserId())
            .status(orderEntity.getStatus())
            .build();
    }
}