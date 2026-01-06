package com.example.demo.entity.cakeTableDto.deliveryPersons;

import java.math.BigDecimal;
import java.time.Instant;

public class DeliveryPersonResponseDto {
    private Integer id;
    private String name;
    private String phone;
    private String idCard;
    private String vehicleType;
    private String licensePlate;
    private String status;
    private BigDecimal rating;
    private Integer completedOrders;
    private Instant createdAt;
    private boolean success;
    private String message;

    // 构造函数
    public DeliveryPersonResponseDto() {}

    public DeliveryPersonResponseDto(Integer id, String name, String phone, String idCard, 
                                   String vehicleType, String licensePlate, String status, 
                                   BigDecimal rating, Integer completedOrders, Instant createdAt,
                                   boolean success, String message) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.status = status;
        this.rating = rating;
        this.completedOrders = completedOrders;
        this.createdAt = createdAt;
        this.success = success;
        this.message = message;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Integer completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
