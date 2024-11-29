package kr.co.petfriends.sample.order.application.usecase.impl;

import java.util.List;
import kr.co.petfriends.sample.common.annotation.UseCase;
import kr.co.petfriends.sample.order.application.port.OrderDataPort;
import kr.co.petfriends.sample.order.application.usecase.OrderUseCase;
import kr.co.petfriends.sample.order.application.usecase.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.application.usecase.dto.OrderResponse;
import kr.co.petfriends.sample.order.application.usecase.mapper.OrderMapper;
import kr.co.petfriends.sample.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
class OrderUseCaseImpl implements OrderUseCase {

    private final OrderDataPort orderPort;

    @Override
    @Transactional
    public OrderResponse requestOrder(CreateOrderCommand command) {
        Order order = OrderMapper.toDomain(command);
        Order requestedOrder = orderPort.save(order);
        return OrderMapper.toResponse(requestedOrder);
    }

    @Override
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderPort.findByCode(orderCode)
            .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderPort.findAll().stream()
            .map(OrderMapper::toResponse)
            .toList();
    }
}
