package kr.co.petfriends.sample.order.application.usecase;

import java.util.List;
import kr.co.petfriends.sample.order.application.usecase.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.application.usecase.dto.OrderResponse;

public interface OrderUseCase {

    OrderResponse requestOrder(CreateOrderCommand command);

    OrderResponse getOrderByCode(String orderCode);

    List<OrderResponse> getAllOrders();
}
