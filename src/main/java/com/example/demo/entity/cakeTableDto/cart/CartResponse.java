package com.example.demo.entity.cakeTableDto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * 购物车响应DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private final boolean success;
    private final String message;
    private final List<CartItemDto> items;
    private final CartStatsDto stats;
    
    public CartResponse(boolean success, String message, List<CartItemDto> items, CartStatsDto stats) {
        this.success = success;
        this.message = message;
        this.items = items;
        this.stats = stats;
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<CartItemDto> getItems() { return items; }
    public CartStatsDto getStats() { return stats; }
}