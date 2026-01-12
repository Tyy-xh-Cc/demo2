package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.*;
import com.example.demo.entity.cakeTableDto.user.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    @GetMapping("/user/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }

        String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
        UserDto userDto = service.getUserByToken(token);

        if (userDto == null) {
            return ResponseEntity.status(401).body(new LoginResponse<>(false, "Token无效或已过期"));
        }

        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/user/stats")
    public ResponseEntity<?> getUserStats(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }

        String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

        // 根据token获取用户统计信息
        StatsResponse statsResponse = service.getUserStatsByToken(token);

        if (statsResponse == null || !statsResponse.isSuccess()) {
            return ResponseEntity.badRequest().body(
                    new LoginResponse<>(false, statsResponse != null ? statsResponse.getMessage() : "获取统计信息失败")
            );
        }

        return ResponseEntity.ok(statsResponse);
    }
    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(new LoginResponse<>(false, "未授权访问"));
        }

        String token = authHeader.substring(7);
        boolean result = service.logout(token);

        if (result) {
            return ResponseEntity.ok(new LoginResponse<>(true, "登出成功"));
        } else {
            return ResponseEntity.badRequest().body(new LoginResponse<>(false, "Token无效"));
        }
    }
    @PostMapping("/auth/login")
    public LoginResponse<?> rootlogin(@RequestBody LoginDto loginDto) {
        return service.rootlogin(loginDto);
    }
    @PostMapping("/user/login")
    public LoginResponse<?> userlogin(@RequestBody LoginDto loginDto) {
        return service.userlogin(loginDto);
    }
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }
    @GetMapping("/paged/users")
    public PageResponse<UserDto> getPagedUsersByConditions(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String identity,
            @RequestParam(required = false) String status) {

        PageRequest pageRequest = new PageRequest(page, size);
        UserQueryDto queryDto = new UserQueryDto(name, phone, identity, status);

        return service.getUsersByConditions(pageRequest, queryDto);
    }

    @PostMapping("/users/{userId}/recharge")
    public ResponseEntity<RechargeResponse> rechargeUser(
            @PathVariable Integer userId,
             @RequestBody BalanceDto balanceDto) {
        RechargeResponse response = service.rechargeUser(userId, balanceDto);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UpdateStatusResponse> updateUserStatus(
            @PathVariable Integer userId,
            @Valid @RequestBody StatusDto updateRequest) {

        UpdateStatusResponse response = service.updateUserStatus(userId, updateRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

}