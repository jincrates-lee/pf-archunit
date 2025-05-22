package kr.co.petfriends.sample.order.domain.model;

import java.time.LocalDateTime;
import kr.co.petfriends.sample.common.annotation.DomainModel;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.domain.exception.InvalidOrderStateException;
import lombok.Builder;

@Builder
@DomainModel
public record Order(
    Long id,
    String code,
    String userId,
    Integer quantity,
    String description,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public Order updateStatus(OrderStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new InvalidOrderStateException(
                String.format("주문 상태를 %s에서 %s로 변경할 수 없습니다.",
                    status.getDescription(), newStatus.getDescription())
            );
        }

        return this.toBuilder()
            .status(newStatus)
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public Order update(String userId, Integer quantity, String description) {
        return this.toBuilder()
            .userId(userId)
            .quantity(quantity)
            .description(description)
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public boolean canCancel() {
        return status.isCancellable();
    }

    public boolean canConfirm() {
        return status.isConfirmable();
    }

    public Order cancel() {
        if (!canCancel()) {
            throw new InvalidOrderStateException(
                String.format("현재 상태(%s)에서는 주문을 취소할 수 없습니다.", status.getDescription())
            );
        }
        return updateStatus(OrderStatus.CANCELLED);
    }

    public Order confirm() {
        if (!canConfirm()) {
            throw new IllegalStateException(
                String.format("현재 상태(%s)에서는 주문을 확정할 수 없습니다.", status.getDescription())
            );
        }
        return updateStatus(OrderStatus.CONFIRMED);
    }
}
