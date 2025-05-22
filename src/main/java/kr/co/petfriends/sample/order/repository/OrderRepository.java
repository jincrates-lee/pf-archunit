package kr.co.petfriends.sample.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.model.Order;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByCode(String orderCode);

    List<Order> findAll();

    void deleteByCode(String orderCode);

    List<Order> findByUserId(String userId);

    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);

    List<Order> searchOrders(OrderStatus status, String userId, LocalDateTime startDate, LocalDateTime endDate);
}
