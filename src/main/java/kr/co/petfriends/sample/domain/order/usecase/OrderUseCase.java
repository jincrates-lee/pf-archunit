package kr.co.petfriends.sample.domain.order.usecase;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.domain.order.model.Order;

public interface OrderUseCase {

    Order requestOrder(Order order);

    Order getOrderByCode(String orderCode);

    Optional<Order> findOrderByCode(String orderCode);

    List<Order> getAllOrders();
}
