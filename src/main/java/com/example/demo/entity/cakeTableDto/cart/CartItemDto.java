package com.example.demo.entity.cakeTableDto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * 购物车项DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto implements Serializable {
    private final Integer id;
    private final Integer userId;
    private final Integer restaurantId;
    private final String restaurantName;
    private final Integer productId;
    private final String productName;
    private final String productImageUrl;
    private final BigDecimal productPrice;
    private final BigDecimal originalPrice;
    private final Integer stock;
    private final Integer quantity;
    private final BigDecimal totalPrice;
    private final String specifications;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Boolean available; // 商品是否可用（库存、状态等）

    public CartItemDto(Integer id, Integer userId, Integer restaurantId, String restaurantName,
                      Integer productId, String productName, String productImageUrl,
                      BigDecimal productPrice, BigDecimal originalPrice, Integer stock,
                      Integer quantity, BigDecimal totalPrice, String specifications,
                      Instant createdAt, Instant updatedAt, Boolean available) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.originalPrice = originalPrice;
        this.stock = stock;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.specifications = specifications;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.available = available;
    }

    // Getters
    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public Integer getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductImageUrl() { return productImageUrl; }
    public BigDecimal getProductPrice() { return productPrice; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public Integer getStock() { return stock; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getSpecifications() { return specifications; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Boolean getAvailable() { return available; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemDto that = (CartItemDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}