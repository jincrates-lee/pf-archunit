package kr.co.petfriends.sample.order.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.order.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByCode(String orderCode);

    List<OrderEntity> findAllByUserId(String userId);
}
