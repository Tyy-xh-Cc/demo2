package com.example.demo.entity.cakeTableDto.restaurant;

public class RestaurantQueryDto {
    private String name;
    private String phone;
    private String address;
    private String status;
    private Double minRating;
    private Double maxRating;

    // 构造函数
    public RestaurantQueryDto() {}

    public RestaurantQueryDto(String name, String phone, String address, String status, Double minRating, Double maxRating) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    // Getter和Setter
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Double maxRating) {
        this.maxRating = maxRating;
    }

    // 判断查询条件是否为空
    public boolean isEmpty() {
        return (name == null || name.trim().isEmpty()) &&
               (phone == null || phone.trim().isEmpty()) &&
               (address == null || address.trim().isEmpty()) &&
               (status == null || status.trim().isEmpty()) &&
               minRating == null && maxRating == null;
    }
}