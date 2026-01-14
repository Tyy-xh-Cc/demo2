package com.example.demo.Service;

import com.example.demo.Controller.SmsController;
import com.example.demo.Repository.AddressRepository;
import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.OrderRepository;
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
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private static final HashMap<String,Integer> userToken=new HashMap<>();

    // 修改构造函数，注入所需的Repository
    public UserService(UserRepository repository,
                       OrderRepository orderRepository,
                       CartItemRepository cartItemRepository,
                       AddressRepository addressRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
    }
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    public UserDto getUserByToken(String token) {
        // 从token映射中获取用户ID
        Integer userId = userToken.get(token);
        if (userId == null) {
            log.warn("Token无效或已过期: {}", token);
            return null;
        }

        // 根据用户ID查询用户信息
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isEmpty()) {
            log.error("Token对应的用户不存在，用户ID: {}", userId);
            userToken.remove(token); // 清理无效的token
            return null;
        }

        User user = userOptional.get();

        // 检查用户状态
        if ("banned".equals(user.getStatus())) {
            log.warn("用户状态异常，无法获取信息。用户ID: {}, 状态: {}", userId, user.getStatus());
            return null;
        }

        // 转换为UserDto返回
        return convertToUserDto(user);
    }
    /**
     * 根据Token获取用户统计信息
     * @param token 用户Token
     * @return StatsResponse 包含统计信息或错误信息
     */
    public StatsResponse getUserStatsByToken(String token) {
        Integer userId = userToken.get(token);
        if (userId == null) {
            return StatsResponse.error("Token无效或已过期");
        }

        return getUserStats(userId);
    }

    /**
     * 获取用户统计信息
     * @param userId 用户ID
     * @return StatsResponse 包含统计信息或错误信息
     */
    public StatsResponse getUserStats(Integer userId) {
        try {
            // 1. 验证用户存在
            Optional<User> userOptional = repository.findById(userId);
            if (userOptional.isEmpty()) {
                return StatsResponse.error("用户不存在");
            }

            User user = userOptional.get();

            // 2. 查询订单统计
            Long totalOrders = orderRepository.countByUser(user);
            Long pendingOrders = orderRepository.countByUserAndStatus(user, "pending");
            Long deliveringOrders = orderRepository.countByUserAndStatus(user, "delivering");
            Long completedOrders = orderRepository.countByUserAndStatus(user, "completed");
            Long cancelledOrders = orderRepository.countByUserAndStatus(user, "cancelled");

            // 3. 查询消费统计
            BigDecimal totalSpent = orderRepository.sumFinalAmountByUser(user);
            if (totalSpent == null) {
                totalSpent = BigDecimal.ZERO;
            }

            // 4. 计算平均订单金额
            BigDecimal averageOrderValue = BigDecimal.ZERO;
            if (totalOrders != null && totalOrders > 0 && totalSpent.compareTo(BigDecimal.ZERO) > 0) {
                averageOrderValue = totalSpent.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
            }

            // 5. 获取最近下单时间
            Instant lastOrderTime = orderRepository.getLastOrderTimeByUser(user);

            // 6. 查询购物车商品数量
            Long totalCartItems = Long.valueOf(cartItemRepository.countItemsByUserId(user.getId()));

            // 7. 查询地址数量
            Long addressCount = addressRepository.countByUser(user);
            if (addressCount == null) {
                addressCount = 0L;
            }

            // 8. 构建统计DTO
            UserStatsDto stats = new UserStatsDto(
                    user.getId(),
                    user.getUsername(),
                    totalOrders != null ? totalOrders : 0L,
                    pendingOrders != null ? pendingOrders : 0L,
                    deliveringOrders != null ? deliveringOrders : 0L,
                    completedOrders != null ? completedOrders : 0L,
                    cancelledOrders != null ? cancelledOrders : 0L,
                    totalSpent,
                    averageOrderValue,
                    lastOrderTime,
                    totalCartItems,
                    addressCount
            );

            return StatsResponse.success(stats);

        } catch (Exception e) {
            log.error("获取用户统计信息失败，用户ID: {}", userId, e);
            return StatsResponse.error("获取统计信息失败: " + e.getMessage());
        }
    }
    @Transactional
    public boolean logout(String token) {
        if (userToken.containsKey(token)) {
            userToken.remove(token);
            log.info("用户登出成功，Token已移除: {}", token);
            return true;
        }
        return false;
    }

    /**
     * 获取所有活跃的Token（用于调试或管理）
     * @return 活跃Token列表
     */
    public HashMap<String, Integer> getActiveTokens() {
        return new HashMap<>(userToken);
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

    public LoginResponse<?> rootlogin(LoginDto loginDto) {
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
        if (!user.getIdentity().equals("0")) {
            log.warn("Non-root user tried to login: {}", user);
            return new LoginResponse<>(false, "非管理员用户，无法登录");
        }
        String token = generateToken(user);
        userToken.put(token,user.getId());
        return new LoginResponse<>(
                true,
                "登录成功",
                token,
                convertToUserDto(user) // 使用convertToUserDto方法转换
        );
    }
    @Transactional
    public LoginResponse<?> userlogin(LoginDto loginDto) {
        Optional<User> userOptional;
        System.out.println(loginDto.getLogin_type());
        switch (loginDto.getLogin_type()) {
            case "phone_password", "sms" -> userOptional =repository.findByPhone(loginDto.getPhone());
            case "password" -> userOptional =repository.findByUsername(loginDto.getUsername());
            default -> {
                log.error("Unsupported login type: {}", loginDto.getLogin_type());
                return new LoginResponse<>(false, "不支持的登录方式");
            }
        }
            if (userOptional.isEmpty()) {
                log.error("User not found");
                return new LoginResponse<>(false, "用户名或密码错误");
            }
            User user = userOptional.get();
            if (Objects.equals(loginDto.getLogin_type(), "sms"))
            {
                if (!loginDto.getSms_code().equals(SmsController.smsCodes.get(loginDto.getPhone()))) {
                    log.info("User found: {}", user);
                    return new LoginResponse<>(false, "短信验证码错误");
                }
            }else {
                if (!user.getPasswordHash().equals(loginDto.getPasswordHash())) {
                    log.info("User found: {}", user);
                    return new LoginResponse<>(false, "用户名或密码错误");
                }
            }
            String token = generateToken(user);
            userToken.put(token, user.getId());
            return new LoginResponse<>(
                    true,
                    "登录成功",
                    token,
                    convertToUserDto(user) // 使用convertToUserDto方法转换
            );
    }
    // 生成简单的token（实际项目中应该使用JWT）
    private String generateToken(User user) {
        // 这里简化处理，实际应该使用JWT等安全的token生成机制
        return "token_" + user.getId() + "_" + System.currentTimeMillis();
    }
    @Transactional
    public RechargeResponse registerUser(LoginDto loginDto) {
        if (repository.findByUsername(loginDto.getUsername()).isPresent()) {
            return new RechargeResponse(false, "用户名已存在", BigDecimal.ZERO, BigDecimal.ZERO);
        }

        // Check if phone already exists
        if (repository.findByPhone(loginDto.getPhone()).isPresent()) {
            return new RechargeResponse(false, "手机号已注册", BigDecimal.ZERO, BigDecimal.ZERO);
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(loginDto.getUsername());
        newUser.setPasswordHash(loginDto.getPasswordHash());
        newUser.setPhone(loginDto.getPhone());
        newUser.setAvatarUrl("http://localhost:8080/images/user/1.png");
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setStatus("active");
        newUser.setIdentity("1"); // Default to normal user
        newUser.setCreatedAt(Instant.now());
        newUser.setUpdatedAt(Instant.now());

        // Save the new user
        User savedUser = repository.save(newUser);

        // Return success response with initial balance
        return new RechargeResponse(true, "注册成功", BigDecimal.ZERO, BigDecimal.ZERO);
    }
}