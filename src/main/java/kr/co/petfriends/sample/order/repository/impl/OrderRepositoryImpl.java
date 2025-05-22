package kr.co.petfriends.sample.order.repository.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.model.Order;
import kr.co.petfriends.sample.order.repository.OrderJpaRepository;
import kr.co.petfriends.sample.order.repository.OrderRepository;
import kr.co.petfriends.sample.order.repository.entity.OrderEntity;
import kr.co.petfriends.sample.order.repository.mapper.OrderDataMapper;
import lombok.RequiredArgsConstructor;

@Adapter
@RequiredArgsConstructor
class OrderRepositoryImpl implements OrderRepository {

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

    @Override
    public void deleteByCode(String orderCode) {
        repository.deleteByCode(orderCode);
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
            .map(OrderDataMapper::toDomain)
            .toList();
    }

    @Override
    public List<Order> findByUserIdAndStatus(String userId, OrderStatus status) {
        return repository.findByUserIdAndStatus(userId, status).stream()
            .map(OrderDataMapper::toDomain)
            .toList();
    }

    @Override
    public List<Order> searchOrders(OrderStatus status, String userId, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.searchOrders(status, userId, startDate, endDate).stream()
            .map(OrderDataMapper::toDomain)
            .toList();
    }
}
