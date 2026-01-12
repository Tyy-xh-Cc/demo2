package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Order;
import com.example.demo.entity.cakeTable.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
    Long countByUser(User user);

    // 统计用户特定状态订单数量
    Long countByUserAndStatus(User user, String status);

    // 统计用户总消费金额
    @Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.user = :user AND o.paymentStatus = 'paid'")
    BigDecimal sumFinalAmountByUser(@Param("user") User user);

    // 获取用户最近下单时间
    @Query("SELECT MAX(o.createdAt) FROM Order o WHERE o.user = :user")
    Instant getLastOrderTimeByUser(@Param("user") User user);

    // 获取用户的订单列表
    List<Order> findByUser(User user);
}