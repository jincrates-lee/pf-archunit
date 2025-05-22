package kr.co.petfriends.sample.order.controller;

import java.util.List;
import kr.co.petfriends.sample.order.service.OrderService;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.UpdateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> requestOrder(
        @Validated @RequestBody CreateOrderCommand command
    ) {
        OrderResponse response = orderService.requestOrder(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




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

    @PutMapping("/{orderCode}")
    public ResponseEntity<OrderResponse> updateOrder(
        @PathVariable(name = "orderCode") String orderCode,
        @Validated @RequestBody UpdateOrderCommand command
    ) {
        OrderResponse response = orderService.updateOrder(orderCode, command);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderCode}")
    public ResponseEntity<Void> deleteOrder(
        @PathVariable(name = "orderCode") String orderCode
    ) {
        orderService.deleteOrder(orderCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderCode}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
        @PathVariable(name = "orderCode") String orderCode
    ) {
        OrderResponse response = orderService.cancelOrder(orderCode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderCode}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(
        @PathVariable(name = "orderCode") String orderCode
    ) {
        OrderResponse response = orderService.confirmOrder(orderCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(
        @PathVariable(name = "userId") String userId,
        @RequestParam(value = "status", required = false) String status
    ) {
        List<OrderResponse> response = orderService.getOrdersByUser(userId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "userId", required = false) String userId,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate
    ) {
        List<OrderResponse> response = orderService.searchOrders(status, userId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}
