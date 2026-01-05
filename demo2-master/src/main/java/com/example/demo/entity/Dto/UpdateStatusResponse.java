package com.example.demo.entity.Dto;

public class UpdateStatusResponse {
    private boolean success;
    private String message;
    private String oldStatus;
    private String newStatus;
    private Integer userId;

    // 构造函数
    public UpdateStatusResponse() {}

    public UpdateStatusResponse(boolean success, String message, String oldStatus, String newStatus, Integer userId) {
        this.success = success;
        this.message = message;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.userId = userId;
    }

    // Getter和Setter
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

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}