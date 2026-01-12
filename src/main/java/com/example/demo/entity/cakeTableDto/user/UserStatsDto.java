package com.example.demo.entity.cakeTableDto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * 用户统计信息DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatsDto implements Serializable {
    private final Integer userId;
    private final String username;
    private final Long totalOrders;          // 总订单数
    private final Long pendingOrders;        // 待处理订单数
    private final Long deliveringOrders;     // 配送中订单数
    private final Long completedOrders;      // 已完成订单数
    private final Long cancelledOrders;      // 已取消订单数
    private final BigDecimal totalSpent;     // 总消费金额
    private final BigDecimal averageOrderValue; // 平均订单金额
    private final Instant lastOrderTime;     // 最近下单时间
    private final Long totalCartItems;       // 购物车商品数量
    private final Long addressCount;         // 地址数量

    public UserStatsDto(Integer userId, String username, Long totalOrders, 
                       Long pendingOrders, Long deliveringOrders, Long completedOrders, 
                       Long cancelledOrders, BigDecimal totalSpent, BigDecimal averageOrderValue, 
                       Instant lastOrderTime, Long totalCartItems, Long addressCount) {
        this.userId = userId;
        this.username = username;
        this.totalOrders = totalOrders;
        this.pendingOrders = pendingOrders;
        this.deliveringOrders = deliveringOrders;
        this.completedOrders = completedOrders;
        this.cancelledOrders = cancelledOrders;
        this.totalSpent = totalSpent;
        this.averageOrderValue = averageOrderValue;
        this.lastOrderTime = lastOrderTime;
        this.totalCartItems = totalCartItems;
        this.addressCount = addressCount;
    }

    // Getter方法
    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public Long getDeliveringOrders() {
        return deliveringOrders;
    }

    public Long getCompletedOrders() {
        return completedOrders;
    }

    public Long getCancelledOrders() {
        return cancelledOrders;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public Instant getLastOrderTime() {
        return lastOrderTime;
    }

    public Long getTotalCartItems() {
        return totalCartItems;
    }

    public Long getAddressCount() {
        return addressCount;
    }
}