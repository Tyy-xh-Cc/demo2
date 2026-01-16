package com.example.demo.entity.cakeTableDto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.OrderItem}
 */
public class OrderItemDto implements Serializable {
    private final Integer id;
    @NotNull
    private final Integer productId;
    @NotNull
    @Size(max = 100)
    private final String productName;
    private final String productImage;
    @NotNull
    private final Integer quantity;
    @NotNull
    private final BigDecimal unitPrice;
    @NotNull
    private final BigDecimal totalPrice;
    private final String specifications;

    public OrderItemDto(Integer id,String productImage, Integer productId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice, String specifications) {
        this.id = id;
        this.productImage = productImage;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.specifications = specifications;
    }

    public Integer getId() {
        return id;
    }


    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getSpecifications() {
        return specifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto entity = (OrderItemDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.productName, entity.productName) &&
                Objects.equals(this.quantity, entity.quantity) &&
                Objects.equals(this.unitPrice, entity.unitPrice) &&
                Objects.equals(this.totalPrice, entity.totalPrice) &&
                Objects.equals(this.specifications, entity.specifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, quantity, unitPrice, totalPrice, specifications);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "productId = " + productId + ", " +
                "productName = " + productName + ", " +
                "quantity = " + quantity + ", " +
                "unitPrice = " + unitPrice + ", " +
                "totalPrice = " + totalPrice + ", " +
                "specifications = " + specifications + ")";
    }

    public String getProductImage() {
        return productImage;
    }
}