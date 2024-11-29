package kr.co.petfriends.sample.order.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.order.domain.model.Order;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByCode(String orderCode);

    List<Order> findAll();
}
