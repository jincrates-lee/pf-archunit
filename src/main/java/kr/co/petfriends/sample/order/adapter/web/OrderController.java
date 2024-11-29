package kr.co.petfriends.sample.order.adapter.web;

import java.net.URI;
import java.util.List;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.order.application.usecase.OrderUseCase;
import kr.co.petfriends.sample.order.application.usecase.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.application.usecase.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Adapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> requestOrder(
        @Validated @RequestBody CreateOrderCommand command
    ) {
        OrderResponse response = orderUseCase.requestOrder(command);
        return ResponseEntity.created(URI.create("/orders/" + response.code()))
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderUseCase.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderResponse> getOrder(
        @PathVariable(name = "orderCode") String orderCode
    ) {
        OrderResponse response = orderUseCase.getOrderByCode(orderCode);
        return ResponseEntity.ok(response);
    }
}
