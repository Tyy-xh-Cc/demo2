package com.example.demo.entity.cakeTableDto.cart;

import java.math.BigDecimal;

/**
 * 购物车请求DTO
 */
public class CartItemRequestDto {
    private Integer productId;
    private Integer quantity;
    private String specifications;
    
    public CartItemRequestDto() {}
    
    public CartItemRequestDto(Integer productId, Integer quantity, String specifications) {
        this.productId = productId;
        this.quantity = quantity;
        this.specifications = specifications;
    }
    
    // Getters and Setters
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getSpecifications() { return specifications; }
    public void setSpecifications(String specifications) { this.specifications = specifications; }
}