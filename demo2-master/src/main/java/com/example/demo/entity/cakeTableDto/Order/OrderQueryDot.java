package com.example.demo.entity.cakeTableDto.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Order}
 */
public class OrderQueryDot implements Serializable {
    @Size(max = 32)
    private final String orderId;
    @NotNull
    @Size(max = 50)
    private final String deliveryName;
    private final String status;

    public OrderQueryDot(String orderId, String deliveryName, String status) {
        this.orderId = orderId;
        this.deliveryName = deliveryName;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderQueryDot entity = (OrderQueryDot) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.deliveryName, entity.deliveryName) &&
                Objects.equals(this.status, entity.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, deliveryName, status);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "orderId = " + orderId + ", " +
                "deliveryName = " + deliveryName + ", " +
                "status = " + status + ")";
    }

    public boolean isEmpty() {
        return (orderId == null || orderId.trim().isEmpty()) &&
                (deliveryName == null || deliveryName.trim().isEmpty()) &&
                (status == null || status.trim().isEmpty());
    }
}