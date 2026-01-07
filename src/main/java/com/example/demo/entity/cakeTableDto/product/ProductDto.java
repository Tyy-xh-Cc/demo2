package com.example.demo.entity.cakeTableDto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Product}
 */
public class ProductDto implements Serializable {
    private final Integer id;
    private final Integer categoryId;
    @NotNull
    @Size(max = 100)
    private final String name;
    private final String description;
    @Size(max = 500)
    private final String imageUrl;
    @NotNull
    private final BigDecimal price;
    private final BigDecimal originalPrice;
    private final Integer stock;
    private final Integer salesCount;
    private final Integer sortOrder;
    private final String status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public ProductDto(Integer id, Integer categoryId, String name, String description, String imageUrl, BigDecimal price, BigDecimal originalPrice, Integer stock, Integer salesCount, Integer sortOrder, String status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.originalPrice = originalPrice;
        this.stock = stock;
        this.salesCount = salesCount;
        this.sortOrder = sortOrder;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto entity = (ProductDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.categoryId, entity.categoryId) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.imageUrl, entity.imageUrl) &&
                Objects.equals(this.price, entity.price) &&
                Objects.equals(this.originalPrice, entity.originalPrice) &&
                Objects.equals(this.stock, entity.stock) &&
                Objects.equals(this.salesCount, entity.salesCount) &&
                Objects.equals(this.sortOrder, entity.sortOrder) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.updatedAt, entity.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryId, name, description, imageUrl, price, originalPrice, stock, salesCount, sortOrder, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "categoryId = " + categoryId + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "imageUrl = " + imageUrl + ", " +
                "price = " + price + ", " +
                "originalPrice = " + originalPrice + ", " +
                "stock = " + stock + ", " +
                "salesCount = " + salesCount + ", " +
                "sortOrder = " + sortOrder + ", " +
                "status = " + status + ", " +
                "createdAt = " + createdAt + ", " +
                "updatedAt = " + updatedAt + ")";
    }
}