package kr.co.petfriends.sample.domain;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.UseCase;
import kr.co.petfriends.sample.common.exception.DomainException;
import kr.co.petfriends.sample.domain.model.Order;
import kr.co.petfriends.sample.domain.port.OrderDataPort;
import kr.co.petfriends.sample.domain.usecase.OrderUseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
class OrderUseCaseImpl implements OrderUseCase {

    private final OrderDataPort repository;

    @Override
    public Order requestOrder(Order order) {
        return repository.save(order);
    }

    @Override
    public Order getOrderByCode(String orderCode) {
        return findOrderByCode(orderCode)
            .orElseThrow(() -> new DomainException("주문 정보가 존재하지 않습니다. 주문번호: " + orderCode));
    }

    @Override
    public Optional<Order> findOrderByCode(String orderCode) {
        return repository.findByCode(orderCode);
    }

    @Override
    public List<Order> getAllOrders() {
        return repository.findAll();
    }
}
