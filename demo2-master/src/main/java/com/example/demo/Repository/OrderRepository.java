package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("SELECT u FROM User u WHERE " +
            "(:order_id IS NULL OR u.username LIKE %:order_id%) AND " +
            "(:delivery_name IS NULL OR u.phone LIKE %:delivery_name%) AND " +
            "(:status IS NULL OR u.status = :status)")
    Page<Order> findByConditions(@Param("order_id") String orderId,@Param("delivery_name") String deliveryName,@Param("status") String status, Pageable pageable);
}