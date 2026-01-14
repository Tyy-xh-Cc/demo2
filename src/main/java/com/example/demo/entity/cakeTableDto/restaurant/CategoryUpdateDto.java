package com.example.demo.entity.cakeTableDto.restaurant;

import jakarta.validation.constraints.Size;

/**
 * 更新分类DTO
 */
public class CategoryUpdateDto {
    @Size(max = 50, message = "分类名称不能超过50个字符")
    private String name;
    
    private Integer sortOrder;
    
    public CategoryUpdateDto() {}
    
    public CategoryUpdateDto(String name, Integer sortOrder) {
        this.name = name;
        this.sortOrder = sortOrder;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}