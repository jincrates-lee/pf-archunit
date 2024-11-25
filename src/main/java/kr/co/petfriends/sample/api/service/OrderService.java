package kr.co.petfriends.sample.api.service;

import java.util.List;
import kr.co.petfriends.sample.api.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.api.service.dto.OrderResponse;
import kr.co.petfriends.sample.api.service.mapper.OrderMapper;
import kr.co.petfriends.sample.domain.order.model.Order;
import kr.co.petfriends.sample.domain.order.usecase.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderUseCase orderUseCase;

    @Transactional
    public OrderResponse requestOrder(CreateOrderCommand command) {
        Order order = OrderMapper.toDomain(command);
        Order requestedOrder = orderUseCase.requestOrder(order);
        return OrderMapper.toResponse(requestedOrder);
    }

    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderUseCase.getOrderByCode(orderCode);
        return OrderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderUseCase.getAllOrders().stream()
            .map(OrderMapper::toResponse)
            .toList();
    }
}
