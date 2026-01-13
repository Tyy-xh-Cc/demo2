package com.example.demo.entity.cakeTableDto.user;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.example.demo.entity.cakeTable.User}
 */
public class LoginDto implements Serializable {
    @Size(max = 50)
    private String username;

    @Size(max = 255)
    private String passwordHash;

    private final String phone;
    private final String sms_code;
    private final String login_type;
    public LoginDto(String username, String passwordHash, String phone, String sms_code, String login_type) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.sms_code = sms_code;
        this.login_type = login_type;
    }
    public String getPhone() {
        return phone;
    }

    public String getSms_code() {
        return sms_code;
    }

    public String getLogin_type() {
        return login_type;
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