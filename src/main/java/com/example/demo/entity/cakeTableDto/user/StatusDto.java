package com.example.demo.entity.cakeTableDto.user;

import com.example.demo.entity.cakeTable.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link User}
 */
public class StatusDto implements Serializable {
    @NotNull(message = "不能为空")
    @Pattern(regexp = "^(active|inactive|banned|pending)$",
            message = "状态必须是 active, inactive, banned 或 pending")

    private final String status;

    public StatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusDto entity = (StatusDto) o;
        return Objects.equals(this.status, entity.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "status = " + status + ")";
    }
}