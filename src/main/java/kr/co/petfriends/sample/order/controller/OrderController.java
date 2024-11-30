package kr.co.petfriends.sample.order.controller;

import java.util.List;
import kr.co.petfriends.sample.order.service.OrderService;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 기존 코드
    @PostMapping
    public ResponseEntity<OrderResponse> requestOrder(
        @Validated @RequestBody CreateOrderCommand command
    ) {
        OrderResponse response = orderService.requestOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 변경 코드
//    @PostMapping
//    public ResponseEntity<Order> requestOrder(
//        @Validated @RequestBody CreateOrderCommand command
//    ) {
//        OrderResponse response = orderService.requestOrder(command);
//
//        // controller -> domain에 대한 의존성이 생긴 경우
//        Order order = Order.builder()
//            .code(response.code())
//            .userId(response.userId())
//            .status(response.status())
//            .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(order);
//    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderResponse> getOrder(
        @PathVariable(name = "orderCode") String orderCode
    ) {
        OrderResponse response = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(response);
    }
}
