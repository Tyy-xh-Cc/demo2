package com.example.demo.entity.cakeTableDto.order;

public class OrderQueryRequest {
    private Integer page = 1; // 页码，默认第1页
    private Integer size = 10; // 每页大小，默认10条
    private String status; // 订单状态筛选
    private String sortBy = "createdAt"; // 排序字段，默认按创建时间排序
    private String sortOrder = "desc"; // 排序方向，默认降序

    // Getters and Setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}