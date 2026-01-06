package com.example.demo.entity.cakeTableDto.deliveryPersons;

public class DeliveryPersonQueryDto {
    private String name;        // 配送员姓名模糊查询
    private String phone;       // 手机号模糊查询
    private String status;      // 状态精确查询
    private String vehicleType; // 车辆类型模糊查询
    private Double minRating;   // 最低评分
    private Double maxRating;   // 最高评分

    // 构造函数
    public DeliveryPersonQueryDto() {}

    public DeliveryPersonQueryDto(String name, String phone, String status, String vehicleType, Double minRating, Double maxRating) {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.vehicleType = vehicleType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
               (status == null || status.trim().isEmpty()) &&
               (vehicleType == null || vehicleType.trim().isEmpty()) &&
               minRating == null && maxRating == null;
    }
}