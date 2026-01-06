package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.Dto.*;
import com.example.demo.entity.cakeTable.User;
import com.example.demo.entity.cakeTableDto.user.*;
import com.example.demo.utility.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Transactional
    public UpdateStatusResponse updateUserStatus(Integer userId, StatusDto updateRequest) {
        // 查找用户
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            return new UpdateStatusResponse(false, "用户不存在", null, updateRequest.getStatus(), userId);
        }

        User user = userOptional.get();
        String oldStatus = user.getStatus();
        String newStatus = updateRequest.getStatus();

        // 检查状态是否相同
        if (oldStatus != null && oldStatus.equals(newStatus)) {
            return new UpdateStatusResponse(false, "用户状态未改变", oldStatus, newStatus, userId);
        }

        // 验证状态转换的合法性
        if (!isValidStatusTransition(oldStatus, newStatus)) {
            return new UpdateStatusResponse(false, "状态转换不合法", oldStatus, newStatus, userId);
        }

        // 更新用户状态
        user.setStatus(newStatus);
        repository.save(user);

        return new UpdateStatusResponse(true, "用户状态更新成功", oldStatus, newStatus, userId);
    }

    private boolean isValidStatusTransition(String oldStatus, String newStatus) {

        if (oldStatus == null) {
            return true; // 初始状态可以设置为任何状态
        }

        return switch (oldStatus) {
            case "active" -> "inactive".equals(newStatus) || "banned".equals(newStatus);
            case "inactive" -> "active".equals(newStatus) || "banned".equals(newStatus);
            case "banned" -> "active".equals(newStatus);
            default -> false;
        };
    }

    @Transactional
    public RechargeResponse rechargeUser(Integer userId, BalanceDto balanceDto) {
        // 查找用户
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            return new RechargeResponse(false, "用户不存在", BigDecimal.ZERO, balanceDto.getBalance());
        }

        User user = userOptional.get();

        // 检查用户状态
        if ("inactive".equals(user.getStatus()) || "banned".equals(user.getStatus())) {
            return new RechargeResponse(false, "用户状态异常，无法充值", user.getBalance(), balanceDto.getBalance());
        }

        // 执行充值
        BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.add(balanceDto.getBalance());

        // 更新用户余额
        user.setBalance(newBalance);
        repository.save(user);

        return new RechargeResponse(true, "充值成功", newBalance, balanceDto.getBalance());
    }
    // 条件分页查询
    public PageResponse<UserDto> getUsersByConditions(PageRequest pageRequest, UserQueryDto queryDto) {
        Pageable pageable = buildPageable(pageRequest);
        Page<User> userPage;

        // 根据查询条件选择不同的查询方法
        if (queryDto.isEmpty()) {
            // 如果没有查询条件，查询所有用户
            userPage = repository.findAll(pageable);
        } else {
            // 使用复杂条件查询
            userPage = repository.findByConditions(
                    queryDto.getName(),
                    queryDto.getPhone(),
                    queryDto.getIdentity(),
                    queryDto.getStatus(),
                    pageable
            );
        }

        List<UserDto> userDtos = userPage.getContent().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());

        return convertToPageResponse(userPage, userDtos);
    }
    // 将User转换为UserDto的私有方法
    private UserDto convertToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getStatus(),
                user.getIdentity()
        );
    }

    public LoginResponse<?> login(LoginDto loginDto) {
        Optional<User> userOptional = repository.findByUsername(loginDto.getUsername());
        if (userOptional.isEmpty()) {
            log.error("User not found");
            return new LoginResponse<>(false, "用户名或密码错误");
        }
        User user = userOptional.get();
        if (!user.getPasswordHash().equals(loginDto.getPasswordHash())) {
            log.info("User found: {}", user);
            return new LoginResponse<>(false, "用户名或密码错误");
        }
        if (!user.getIdentity().equals("3")) {
            log.warn("Non-root user tried to login: {}", user);
            return new LoginResponse<>(false, "非管理员用户，无法登录");
        }
        String token = generateToken(user);


        return new LoginResponse<>(
                true,
                "登录成功",
                token,
                user
        );
    }

    // 生成简单的token（实际项目中应该使用JWT）
    private String generateToken(User user) {
        // 这里简化处理，实际应该使用JWT等安全的token生成机制
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
}
