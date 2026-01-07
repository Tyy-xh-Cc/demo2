// ProductQueryDto.java - 产品查询条件DTO
package com.example.demo.entity.cakeTableDto.product;

import java.math.BigDecimal;

public class ProductQueryDto {
    private String name;
    private Integer restaurantId;
    private String restaurantName;
    private Integer categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Integer maxStock;
    private String status;

    // 构造方法
    public ProductQueryDto() {}

    public ProductQueryDto(String name, Integer restaurantId, String restaurantName, 
                          Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, 
                          Integer minStock, Integer maxStock, String status) {
        this.name = name;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.status = status;
    }

    // 检查查询条件是否为空
    public boolean isEmpty() {
        return name == null && restaurantId == null && restaurantName == null && 
               categoryId == null && minPrice == null && maxPrice == null && 
               minStock == null && maxStock == null && status == null;
    }

    // Getter和Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }
    
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer minStock) { this.minStock = minStock; }
    
    public Integer getMaxStock() { return maxStock; }
    public void setMaxStock(Integer maxStock) { this.maxStock = maxStock; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}