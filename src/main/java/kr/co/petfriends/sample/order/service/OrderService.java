package kr.co.petfriends.sample.order.service;

import java.util.List;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.UpdateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;

public interface OrderService {

    OrderResponse requestOrder(CreateOrderCommand command);

    OrderResponse getOrderByCode(String orderCode);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrder(String orderCode, UpdateOrderCommand command);

    void deleteOrder(String orderCode);

    OrderResponse cancelOrder(String orderCode);

    OrderResponse confirmOrder(String orderCode);

    List<OrderResponse> getOrdersByUser(String userId, String status);

    List<OrderResponse> searchOrders(String status, String userId, String startDate, String endDate);
}
