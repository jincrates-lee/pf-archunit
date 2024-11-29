package kr.co.petfriends.sample.order.service.impl;

import java.util.List;
import kr.co.petfriends.sample.common.annotation.UseCase;
import kr.co.petfriends.sample.order.domain.model.Order;
import kr.co.petfriends.sample.order.repository.OrderRepository;
import kr.co.petfriends.sample.order.service.OrderService;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;
import kr.co.petfriends.sample.order.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    @Transactional
    public OrderResponse requestOrder(CreateOrderCommand command) {
        Order order = OrderMapper.toDomain(command);
        Order requestedOrder = repository.save(order);
        return OrderMapper.toResponse(requestedOrder);
    }

    @Override
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
            .map(OrderMapper::toResponse)
            .toList();
    }
}
