package com.guavapay.deliveryservice.repository;

import com.guavapay.deliveryservice.entity.DeliveryOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<DeliveryOrder, Long> {

    public List<DeliveryOrder> findAllByOwnerId(Long userId, Pageable pageable);

    public List<DeliveryOrder> findAllByCourierId(Long courierId, Pageable pageable);

    public Optional<DeliveryOrder> findByIdAndCourierId(Long orderId, Long courierId);
}
