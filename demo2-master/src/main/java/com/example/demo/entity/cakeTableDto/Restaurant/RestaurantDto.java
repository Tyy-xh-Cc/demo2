package com.example.demo.entity.cakeTableDto.Restaurant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Restaurant}
 */
public class RestaurantDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 100)
    private final String name;
    private final String description;
    @Size(max = 500)
    private final String logoUrl;
    @Size(max = 500)
    private final String coverUrl;
    @NotNull
    @Size(max = 20)
    private final String phone;
    @NotNull
    @Size(max = 200)
    private final String address;
    @Size(max = 100)
    private final String openingHours;
    private final BigDecimal minOrderAmount;
    private final BigDecimal deliveryFee;
    private final Integer estimatedDeliveryTime;
    private final BigDecimal rating;
    private final Integer totalOrders;
    private final String status;
    private final Instant createdAt;

    public RestaurantDto(Integer id, String name, String description, String logoUrl, String coverUrl, String phone, String address, String openingHours, BigDecimal minOrderAmount, BigDecimal deliveryFee, Integer estimatedDeliveryTime, BigDecimal rating, Integer totalOrders, String status, Instant createdAt) {
        this.id = id;
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
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDto entity = (RestaurantDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.logoUrl, entity.logoUrl) &&
                Objects.equals(this.coverUrl, entity.coverUrl) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.openingHours, entity.openingHours) &&
                Objects.equals(this.minOrderAmount, entity.minOrderAmount) &&
                Objects.equals(this.deliveryFee, entity.deliveryFee) &&
                Objects.equals(this.estimatedDeliveryTime, entity.estimatedDeliveryTime) &&
                Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.totalOrders, entity.totalOrders) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, logoUrl, coverUrl, phone, address, openingHours, minOrderAmount, deliveryFee, estimatedDeliveryTime, rating, totalOrders, status, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "logoUrl = " + logoUrl + ", " +
                "coverUrl = " + coverUrl + ", " +
                "phone = " + phone + ", " +
                "address = " + address + ", " +
                "openingHours = " + openingHours + ", " +
                "minOrderAmount = " + minOrderAmount + ", " +
                "deliveryFee = " + deliveryFee + ", " +
                "estimatedDeliveryTime = " + estimatedDeliveryTime + ", " +
                "rating = " + rating + ", " +
                "totalOrders = " + totalOrders + ", " +
                "status = " + status + ", " +
                "createdAt = " + createdAt + ")";
    }
}