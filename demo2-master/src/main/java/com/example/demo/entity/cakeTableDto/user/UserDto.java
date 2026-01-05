package com.example.demo.entity.cakeTableDto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.User}
 */
public class UserDto implements Serializable {
    private final Integer id;
    @NotNull
    @Size(max = 50)
    private final String username;
    @NotNull
    @Size(max = 20)
    private final String phone;
    @Size(max = 100)
    private final String email;
    @Size(max = 500)
    private final String avatarUrl;
    private final BigDecimal balance;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String status;
    @NotNull
    private final String identity;

    public UserDto(Integer id, String username, String phone, String email, String avatarUrl, BigDecimal balance, Instant createdAt, Instant updatedAt, String status, String identity) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.identity = identity;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto entity = (UserDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.username, entity.username) &&
                Objects.equals(this.phone, entity.phone) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.avatarUrl, entity.avatarUrl) &&
                Objects.equals(this.balance, entity.balance) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.updatedAt, entity.updatedAt) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.identity, entity.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, phone, email, avatarUrl, balance, createdAt, updatedAt, status, identity);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "phone = " + phone + ", " +
                "email = " + email + ", " +
                "avatarUrl = " + avatarUrl + ", " +
                "balance = " + balance + ", " +
                "createdAt = " + createdAt + ", " +
                "updatedAt = " + updatedAt + ", " +
                "status = " + status + ", " +
                "identity = " + identity + ")";
    }
}