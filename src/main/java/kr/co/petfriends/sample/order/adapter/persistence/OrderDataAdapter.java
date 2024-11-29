package kr.co.petfriends.sample.order.adapter.persistence;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.order.adapter.persistence.entity.OrderEntity;
import kr.co.petfriends.sample.order.adapter.persistence.mapper.OrderDataMapper;
import kr.co.petfriends.sample.order.adapter.persistence.repository.OrderJpaRepository;
import kr.co.petfriends.sample.order.application.port.OrderDataPort;
import kr.co.petfriends.sample.order.domain.model.Order;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
class OrderDataAdapter implements OrderDataPort {

    private final OrderJpaRepository repository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderDataMapper.toEntity(order);
        OrderEntity savedEntity = repository.save(entity);
        return OrderDataMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findByCode(String orderCode) {
        return repository.findByCode(orderCode)
            .map(OrderDataMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll().stream()
            .map(OrderDataMapper::toDomain)
            .toList();
    }

    // domain -> infrastructure에 대한 의존성이 생긴 경우
//    @Override
//    public OrderEntity save(OrderEntity orderEntity) {
//        return null;
//    }
}
