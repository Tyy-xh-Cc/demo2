package com.example.demo.entity.cakeTableDto.order;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {
    private Integer restaurantId;
    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryName;
    private String note;
    private String paymentMethod;
    private List<OrderItemRequest> items; // 改为items以匹配前端
    private String deliveryTime; // 配送时间
    private Integer tablewareCount; // 餐具数量
    private String invoiceInfo; // 发票信息
    private Integer couponId; // 优惠券ID

    // Getters and Setters
    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getTablewareCount() {
        return tablewareCount;
    }

    public void setTablewareCount(Integer tablewareCount) {
        this.tablewareCount = tablewareCount;
    }

    public String getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(String invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "restaurantId=" + restaurantId +
                ", totalAmount=" + totalAmount +
                ", deliveryFee=" + deliveryFee +
                ", discountAmount=" + discountAmount +
                ", finalAmount=" + finalAmount +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", deliveryPhone='" + deliveryPhone + '\'' +
                ", deliveryName='" + deliveryName + '\'' +
                ", note='" + note + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", items=" + items +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", tablewareCount=" + tablewareCount +
                ", invoiceInfo='" + invoiceInfo + '\'' +
                ", couponId=" + couponId +
                '}';
    }
}
