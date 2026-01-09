package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.*;
import com.example.demo.entity.cakeTable.User;
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
    @PostMapping("/auth/login")
    public LoginResponse<?> login(@RequestBody LoginDto loginDto) {
        return service.login(loginDto);
    }
    @GetMapping("/users")
    public List<User> getAllUsers() {
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
    /**
     * 用户充值接口
     * @param userId 用户ID
     * @param balanceDto 充值请求
     * @return 充值响应
     */
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

    /**
     * 更新用户状态接口
     * @param userId 用户ID
     * @param updateRequest 更新请求
     * @return 更新响应
     */
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
