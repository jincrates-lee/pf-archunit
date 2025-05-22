package kr.co.petfriends.sample.order.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import kr.co.petfriends.sample.common.annotation.UseCase;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.exception.InvalidOrderStateException;
import kr.co.petfriends.sample.order.domain.exception.OrderNotFoundException;
import kr.co.petfriends.sample.order.domain.model.Order;
import kr.co.petfriends.sample.order.repository.OrderRepository;
import kr.co.petfriends.sample.order.service.OrderService;
import kr.co.petfriends.sample.order.service.dto.CreateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.UpdateOrderCommand;
import kr.co.petfriends.sample.order.service.dto.OrderResponse;
import kr.co.petfriends.sample.order.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    @Transactional
    public OrderResponse requestOrder(CreateOrderCommand command) {
        Order order = OrderMapper.toDomain(command);
        Order requestedOrder = repository.save(order);
        return OrderMapper.toResponse(requestedOrder);
    }

    @Override
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new OrderNotFoundException(orderCode));
        return OrderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
            .map(OrderMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(String orderCode, UpdateOrderCommand command) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new OrderNotFoundException(orderCode));
        
        Order updatedOrder = order.update(command.userId(), command.quantity(), command.description());
        Order savedOrder = repository.save(updatedOrder);
        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(String orderCode) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new OrderNotFoundException(orderCode));
        
        if (!order.canCancel()) {
            throw new InvalidOrderStateException("삭제할 수 없는 상태의 주문입니다: " + order.status().getDescription());
        }
        
        repository.deleteByCode(orderCode);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderCode) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new OrderNotFoundException(orderCode));
        
        Order cancelledOrder = order.cancel();
        Order savedOrder = repository.save(cancelledOrder);
        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrder(String orderCode) {
        Order order = repository.findByCode(orderCode)
            .orElseThrow(() -> new OrderNotFoundException(orderCode));
        
        Order confirmedOrder = order.confirm();
        Order savedOrder = repository.save(confirmedOrder);
        return OrderMapper.toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String userId, String status) {
        List<Order> orders;
        
        if (StringUtils.hasText(status)) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = repository.findByUserIdAndStatus(userId, orderStatus);
        } else {
            orders = repository.findByUserId(userId);
        }
        
        return orders.stream()
            .map(OrderMapper::toResponse)
            .toList();
    }

    @Override
    public List<OrderResponse> searchOrders(String status, String userId, String startDate, String endDate) {
        OrderStatus orderStatus = StringUtils.hasText(status) ? OrderStatus.valueOf(status.toUpperCase()) : null;
        LocalDateTime startDateTime = parseDate(startDate);
        LocalDateTime endDateTime = parseDate(endDate);
        if (endDateTime != null) {
            endDateTime = endDateTime.plusDays(1).minusNanos(1); // 해당 날짜의 마지막 시간
        }
        
        List<Order> orders = repository.searchOrders(orderStatus, userId, startDateTime, endDateTime);
        
        return orders.stream()
            .map(OrderMapper::toResponse)
            .toList();
    }

    private LocalDateTime parseDate(String dateString) {
        if (!StringUtils.hasText(dateString)) {
            return null;
        }
        
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.atStartOfDay();
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식을 사용해주세요: " + dateString);
        }
    }
}
