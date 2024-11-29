package kr.co.petfriends.sample.order.application.port;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.order.domain.model.Order;

public interface OrderDataPort {

    Order save(Order order);

    Optional<Order> findByCode(String orderCode);

    List<Order> findAll();
}
