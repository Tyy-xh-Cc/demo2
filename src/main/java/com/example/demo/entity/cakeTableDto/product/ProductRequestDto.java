package com.example.demo.entity.cakeTableDto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductRequestDto {
    
    @NotNull(message = "餐厅ID不能为空")
    private Integer restaurantId;
    
    private Integer categoryId;
    
    @NotNull(message = "产品名称不能为空")
    @Size(min = 1, max = 100, message = "产品名称长度应在1-100字符之间")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    
    private BigDecimal originalPrice;
    
    private Integer stock = -1; // 默认-1表示无限库存
    
    private Integer salesCount = 0;
    
    private Integer sortOrder = 0;
    
    private String status = "available";

    // Getter和Setter
    public Integer getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Integer restaurantId) { this.restaurantId = restaurantId; }
    
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public Integer getSalesCount() { return salesCount; }
    public void setSalesCount(Integer salesCount) { this.salesCount = salesCount; }
    
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}