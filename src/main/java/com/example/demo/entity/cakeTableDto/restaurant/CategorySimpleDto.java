package com.example.demo.entity.cakeTableDto.restaurant;

import java.io.Serializable;

/**
 * 简单分类信息DTO
 */
public class CategorySimpleDto implements Serializable {
    private final Integer id;
    private final String name;
    private final Integer productCount;
    private final Integer sortOrder;

    public CategorySimpleDto(Integer id, String name, Integer productCount, Integer sortOrder) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
        this.sortOrder = sortOrder;
    }

    // Getters
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Integer getProductCount() { return productCount; }
    public Integer getSortOrder() { return sortOrder; }
}