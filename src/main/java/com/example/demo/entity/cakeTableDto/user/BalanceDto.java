package com.example.demo.entity.cakeTableDto.user;

import com.example.demo.entity.cakeTable.User;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO for {@link User}
 */
public class BalanceDto implements Serializable {
    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "0.01", message = "充值金额必须大于0")
    private final BigDecimal balance;

    public BalanceDto(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceDto entity = (BalanceDto) o;
        return Objects.equals(this.balance, entity.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "balance = " + balance + ")";
    }
}