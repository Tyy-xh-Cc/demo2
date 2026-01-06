package com.example.demo.entity.cakeTableDto.deliveryPersons;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.DeliveryPerson}
 */
public class DeliveryPersonDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 50)
    private final String name;
    @NotNull
    @Size(max = 20)
    private final String phone;
    @Size(max = 20)
    private final String idCard;
    private final String vehicleType;
    @Size(max = 20)
    private final String licensePlate;
    private final String status;
    private final BigDecimal rating;
    private final Integer completedOrders;
    private final Instant createdAt;

    public DeliveryPersonDto(Integer id, String name, String phone, String idCard, String vehicleType, String licensePlate, String status, BigDecimal rating, Integer completedOrders, Instant createdAt) {
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
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public Integer getCompletedOrders() {
        return completedOrders;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryPersonDto entity = (DeliveryPersonDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.idCard, entity.idCard) &&
                Objects.equals(this.vehicleType, entity.vehicleType) &&
                Objects.equals(this.licensePlate, entity.licensePlate) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.rating, entity.rating) &&
                Objects.equals(this.completedOrders, entity.completedOrders) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, idCard, vehicleType, licensePlate, status, rating, completedOrders, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "phone = " + phone + ", " +
                "idCard = " + idCard + ", " +
                "vehicleType = " + vehicleType + ", " +
                "licensePlate = " + licensePlate + ", " +
                "status = " + status + ", " +
                "rating = " + rating + ", " +
                "completedOrders = " + completedOrders + ", " +
                "createdAt = " + createdAt + ")";
    }
}