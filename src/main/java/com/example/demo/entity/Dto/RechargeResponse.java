package com.example.demo.entity.Dto;

import java.math.BigDecimal;

public class RechargeResponse {
    private boolean success;
    private String message;
    private BigDecimal newBalance;
    private BigDecimal rechargeAmount;

    // 构造函数
    public RechargeResponse() {}

    public RechargeResponse(boolean success, String message, BigDecimal newBalance, BigDecimal rechargeAmount) {
        this.success = success;
        this.message = message;
        this.newBalance = newBalance;
        this.rechargeAmount = rechargeAmount;
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

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}