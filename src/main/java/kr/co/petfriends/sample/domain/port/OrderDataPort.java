package kr.co.petfriends.sample.domain.port;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.domain.model.Order;

public interface OrderDataPort {

    Order save(Order order);

    Optional<Order> findByCode(String orderCode);

    List<Order> findAll();

    // domain -> infrastructure에 대한 의존성이 생긴 경우
    //OrderEntity save(OrderEntity orderEntity);
}
