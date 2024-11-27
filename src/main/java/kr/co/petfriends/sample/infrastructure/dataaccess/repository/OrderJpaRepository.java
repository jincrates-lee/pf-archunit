package kr.co.petfriends.sample.infrastructure.dataaccess.repository;

import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.infrastructure.dataaccess.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByCode(String orderCode);

    List<OrderEntity> findAllByUserId(String userId);
}
