package com.example.demo.entity.Dto;

import jakarta.validation.constraints.Min;

public class PageRequest {
    
    @Min(value = 1, message = "页码不能小于1")
    private int page = 1;
    
    @Min(value = 1, message = "每页大小不能小于1")
    private int size = 10;
    
    private String sortBy;
    private String sortDirection = "ASC"; // ASC 或 DESC

    // 构造函数
    public PageRequest() {}

    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageRequest(int page, int size, String sortBy, String sortDirection) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    // Getter和Setter
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    // 计算偏移量
    public int getOffset() {
        return (page - 1) * size;
    }
}