package kr.co.petfriends.sample.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.order.domain.enums.OrderStatus;
import kr.co.petfriends.sample.order.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByCode(String orderCode);

    void deleteByCode(String orderCode);

    List<OrderEntity> findByUserId(String userId);

    List<OrderEntity> findByUserIdAndStatus(String userId, OrderStatus status);

    @Query("SELECT o FROM OrderEntity o WHERE " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR o.createdAt <= :endDate) " +
           "ORDER BY o.createdAt DESC")
    List<OrderEntity> searchOrders(
        @Param("status") OrderStatus status,
        @Param("userId") String userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
