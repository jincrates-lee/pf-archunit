package kr.co.petfriends.sample.order.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(length = 40, nullable = false, updatable = false)
    @Comment("주문자 ID")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Comment("주문 상태")
    private OrderStatus status;

    @Builder
    public OrderEntity(
        Long id,
        String code,
        String userId,
        OrderStatus status
    ) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.status = status;
    }
}
