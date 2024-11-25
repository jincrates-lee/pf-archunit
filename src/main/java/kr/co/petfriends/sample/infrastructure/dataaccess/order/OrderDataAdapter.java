package kr.co.petfriends.sample.infrastructure.dataaccess.order;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.domain.order.model.Order;
import kr.co.petfriends.sample.domain.order.port.OrderRepository;
import kr.co.petfriends.sample.infrastructure.dataaccess.order.entity.OrderEntity;
import kr.co.petfriends.sample.infrastructure.dataaccess.order.mapper.OrderDataMapper;
import kr.co.petfriends.sample.infrastructure.dataaccess.order.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
class OrderDataAdapter implements OrderRepository {

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
}
