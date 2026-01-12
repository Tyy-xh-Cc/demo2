package com.example.demo.entity.cakeTableDto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统计信息响应DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsResponse {
    private final boolean success;
    private final String message;
    private final UserStatsDto stats;

    public StatsResponse(boolean success, String message, UserStatsDto stats) {
        this.success = success;
        this.message = message;
        this.stats = stats;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserStatsDto getStats() {
        return stats;
    }

    // 成功时的静态工厂方法
    public static StatsResponse success(UserStatsDto stats) {
        return new StatsResponse(true, "获取统计信息成功", stats);
    }

    // 失败时的静态工厂方法
    public static StatsResponse error(String message) {
        return new StatsResponse(false, message, null);
    }
}