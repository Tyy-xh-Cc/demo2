package com.example.demo.entity.cakeTableDto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.User}
 */
public class LoginDto implements Serializable {
    @NotNull
    @Size(max = 50)
    private final String username;
    @NotNull
    @Size(max = 255)
    private final String passwordHash;

    public LoginDto(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDto entity = (LoginDto) o;
        return Objects.equals(this.username, entity.username) &&
                Objects.equals(this.passwordHash, entity.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, passwordHash);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "passwordHash = " + passwordHash + ")";
    }
}