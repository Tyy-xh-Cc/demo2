package com.example.demo.entity.cakeTableDto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class RestaurantRequestDto {

    private String name;
    
    private String description;
    
    private String logoUrl;
    
    private String coverUrl;

    private String phone;

    private String address;
    
    private String openingHours;
    
    private BigDecimal minOrderAmount;
    
    private BigDecimal deliveryFee;
    
    private Integer estimatedDeliveryTime;
    
    private BigDecimal rating;
    
    private Integer totalOrders;
    
    private String status;

    // 构造函数
    public RestaurantRequestDto() {}

    public RestaurantRequestDto(String name, String description, String logoUrl, String coverUrl, 
                               String phone, String address, String openingHours, 
                               BigDecimal minOrderAmount, BigDecimal deliveryFee, 
                               Integer estimatedDeliveryTime, BigDecimal rating, 
                               Integer totalOrders, String status) {
        this.name = name;
        this.description = description;
        this.logoUrl = logoUrl;
        this.coverUrl = coverUrl;
        this.phone = phone;
        this.address = address;
        this.openingHours = openingHours;
        this.minOrderAmount = minOrderAmount;
        this.deliveryFee = deliveryFee;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.rating = rating;
        this.totalOrders = totalOrders;
        this.status = status;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
