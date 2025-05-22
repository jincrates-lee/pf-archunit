package kr.co.petfriends.sample.order.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "orders")
@Comment("주문")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    @Comment("PK")
    private Long id;

    @Column(unique = true, length = 40, nullable = false, updatable = false)
    @Comment("주문 코드")
    private String code;

    @Column(length = 40, nullable = false)
    @Comment("주문자 ID")
    private String userId;

    @Column(nullable = false)
    @Comment("수량")
    private Integer quantity;

    @Column(length = 500)
    @Comment("주문 설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Comment("주문 상태")
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @Builder
    public OrderEntity(
        Long id,
        String code,
        String userId,
        Integer quantity,
        String description,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateOrder(String userId, Integer quantity, String description, OrderStatus status, LocalDateTime updatedAt) {
        this.userId = userId;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
