package com.example.demo.entity.cakeTableDto.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Order}
 */
public class OrderDto implements Serializable {
    @Size(max = 32)
    private final String orderId;
    @NotNull
    private final BigDecimal totalAmount;
    private final BigDecimal deliveryFee;
    private final BigDecimal discountAmount;
    @NotNull
    private final BigDecimal finalAmount;
    @NotNull
    @Size(max = 200)
    private final String deliveryAddress;
    @NotNull
    @Size(max = 20)
    private final String deliveryPhone;
    @NotNull
    @Size(max = 50)
    private final String deliveryName;
    private final String note;
    private final String status;
    @NotNull
    private final String paymentMethod;
    private final String paymentStatus;
    private final Instant paidAt;
    private final Instant completedAt;
    private final Instant cancelledAt;
    private final Instant createdAt;

    public OrderDto(String orderId, BigDecimal totalAmount, BigDecimal deliveryFee, BigDecimal discountAmount, BigDecimal finalAmount, String deliveryAddress, String deliveryPhone, String deliveryName, String note, String status, String paymentMethod, String paymentStatus, Instant paidAt, Instant completedAt, Instant cancelledAt, Instant createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.deliveryName = deliveryName;
        this.note = note;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paidAt = paidAt;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public String getNote() {
        return note;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto entity = (OrderDto) o;
        return Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.totalAmount, entity.totalAmount) &&
                Objects.equals(this.deliveryFee, entity.deliveryFee) &&
                Objects.equals(this.discountAmount, entity.discountAmount) &&
                Objects.equals(this.finalAmount, entity.finalAmount) &&
                Objects.equals(this.deliveryAddress, entity.deliveryAddress) &&
                Objects.equals(this.deliveryPhone, entity.deliveryPhone) &&
                Objects.equals(this.deliveryName, entity.deliveryName) &&
                Objects.equals(this.note, entity.note) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.paymentMethod, entity.paymentMethod) &&
                Objects.equals(this.paymentStatus, entity.paymentStatus) &&
                Objects.equals(this.paidAt, entity.paidAt) &&
                Objects.equals(this.completedAt, entity.completedAt) &&
                Objects.equals(this.cancelledAt, entity.cancelledAt) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, totalAmount, deliveryFee, discountAmount, finalAmount, deliveryAddress, deliveryPhone, deliveryName, note, status, paymentMethod, paymentStatus, paidAt, completedAt, cancelledAt, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "orderId = " + orderId + ", " +
                "totalAmount = " + totalAmount + ", " +
                "deliveryFee = " + deliveryFee + ", " +
                "discountAmount = " + discountAmount + ", " +
                "finalAmount = " + finalAmount + ", " +
                "deliveryAddress = " + deliveryAddress + ", " +
                "deliveryPhone = " + deliveryPhone + ", " +
                "deliveryName = " + deliveryName + ", " +
                "note = " + note + ", " +
                "status = " + status + ", " +
                "paymentMethod = " + paymentMethod + ", " +
                "paymentStatus = " + paymentStatus + ", " +
                "paidAt = " + paidAt + ", " +
                "completedAt = " + completedAt + ", " +
                "cancelledAt = " + cancelledAt + ", " +
                "createdAt = " + createdAt + ")";
    }
}