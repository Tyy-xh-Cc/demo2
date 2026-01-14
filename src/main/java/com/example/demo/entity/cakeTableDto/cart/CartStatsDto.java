package com.example.demo.entity.cakeTableDto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

/**
 * 购物车统计信息DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartStatsDto {
    private final Integer totalItems;         // 总商品数量
    private final Integer totalProducts;      // 商品种类数
    private final BigDecimal totalAmount;     // 商品总金额
    private final BigDecimal deliveryFee;     // 配送费
    private final BigDecimal discount;        // 优惠金额
    private final BigDecimal payableAmount;   // 应付金额
    private final Integer restaurantCount;    // 餐厅数量
    
    public CartStatsDto(Integer totalItems, Integer totalProducts, BigDecimal totalAmount,
                       BigDecimal deliveryFee, BigDecimal discount, BigDecimal payableAmount,
                       Integer restaurantCount) {
        this.totalItems = totalItems;
        this.totalProducts = totalProducts;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.payableAmount = payableAmount;
        this.restaurantCount = restaurantCount;
    }
    
    // Getters
    public Integer getTotalItems() { return totalItems; }
    public Integer getTotalProducts() { return totalProducts; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getPayableAmount() { return payableAmount; }
    public Integer getRestaurantCount() { return restaurantCount; }
}