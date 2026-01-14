package com.example.demo.entity.cakeTableDto.restaurant;

import com.example.demo.entity.cakeTableDto.product.ProductDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;

/**
 * 分类详情DTO（包含商品列表）
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDetailDto implements Serializable {
    private final Integer id;
    private final String name;
    private final Integer sortOrder;
    private final Integer productCount;
    private final List<ProductDto> products;
    private final Integer restaurantId;
    private final String restaurantName;

    public CategoryDetailDto(Integer id, String name, Integer sortOrder, 
                            Integer productCount, List<ProductDto> products,
                            Integer restaurantId, String restaurantName) {
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
        this.productCount = productCount;
        this.products = products;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getSortOrder() { return sortOrder; }
    public Integer getProductCount() { return productCount; }
    public List<ProductDto> getProducts() { return products; }
    public Integer getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
}