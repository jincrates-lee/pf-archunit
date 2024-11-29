package kr.co.petfriends.sample.order.service;

import java.util.List;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;

public interface OrderService {

    OrderResponse requestOrder(CreateOrderCommand command);

    OrderResponse getOrderByCode(String orderCode);

    List<OrderResponse> getAllOrders();
}
