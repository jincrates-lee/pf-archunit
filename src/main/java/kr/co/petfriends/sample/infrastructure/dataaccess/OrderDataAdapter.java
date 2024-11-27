package kr.co.petfriends.sample.infrastructure.dataaccess;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.domain.model.Order;
import kr.co.petfriends.sample.domain.port.OrderDataPort;
import kr.co.petfriends.sample.infrastructure.dataaccess.entity.OrderEntity;
import kr.co.petfriends.sample.infrastructure.dataaccess.mapper.OrderDataMapper;
import kr.co.petfriends.sample.infrastructure.dataaccess.repository.OrderJpaRepository;
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
