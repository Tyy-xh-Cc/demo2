package com.example.demo.entity.cakeTableDto.address;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.Address}
 */
public class AddressDto implements Serializable {
    private final Integer id;
    private final Integer userId;
    private final String name;
    private final String receiverName;
    private final String phone;
    private final String address;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final Boolean isDefault;
    private final Instant createdAt;

    public AddressDto(Integer id, Integer userId, String name, String receiverName, String phone, String address, BigDecimal latitude, BigDecimal longitude, Boolean isDefault, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.receiverName = receiverName;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDto entity = (AddressDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.receiverName, entity.receiverName) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.latitude, entity.latitude) &&
                Objects.equals(this.longitude, entity.longitude) &&
                Objects.equals(this.isDefault, entity.isDefault) &&
                Objects.equals(this.createdAt, entity.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, receiverName, phone, address, latitude, longitude, isDefault, createdAt);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "userId = " + userId + ", " +
                "name = " + name + ", " +
                "receiverName = " + receiverName + ", " +
                "phone = " + phone + ", " +
                "address = " + address + ", " +
                "latitude = " + latitude + ", " +
                "longitude = " + longitude + ", " +
                "isDefault = " + isDefault + ", " +
                "createdAt = " + createdAt + ")";
    }
}