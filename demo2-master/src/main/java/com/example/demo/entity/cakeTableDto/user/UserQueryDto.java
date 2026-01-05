package com.example.demo.entity.cakeTableDto.user;

public class UserQueryDto {
    private String name;        // 用户名模糊查询
    private String phone;       // 手机号模糊查询
    private String identity;    // 身份精确查询
    private String status;      // 状态精确查询

    // 构造函数
    public UserQueryDto() {}

    public UserQueryDto(String name, String phone, String identity, String status) {
        this.name = name;
        this.phone = phone;
        this.identity = identity;
        this.status = status;
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 判断查询条件是否为空
    public boolean isEmpty() {
        return (name == null || name.trim().isEmpty()) &&
               (phone == null || phone.trim().isEmpty()) &&
               (identity == null || identity.trim().isEmpty()) &&
               (status == null || status.trim().isEmpty());
    }
}
