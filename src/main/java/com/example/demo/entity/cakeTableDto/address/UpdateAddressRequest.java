package com.example.demo.entity.cakeTableDto.address;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for updating address information
 */
public class UpdateAddressRequest implements Serializable {
    private String name;
    private String receiverName;
    private String phone;
    private String address;
    private String area;
    private Boolean isDefault;
    private BigDecimal latitude;
    private BigDecimal longitude;

    // 默认构造函数（必须）
    public UpdateAddressRequest() {
    }

    // 带参数的构造函数（可选）
    public UpdateAddressRequest(String name, String receiverName, String phone, String address, String area, Boolean isDefault, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.receiverName = receiverName;
        this.phone = phone;
        this.address = address;
        this.area = area;
        this.isDefault = isDefault;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter方法
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

    public String getArea() {
        return area;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    // Setter方法（必须）
    public void setName(String name) {
        this.name = name;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}