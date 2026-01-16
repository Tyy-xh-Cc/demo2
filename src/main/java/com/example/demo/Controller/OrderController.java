package com.example.demo.Controller;

import com.example.demo.Service.OrderService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.Dto.RechargeResponse;
import com.example.demo.entity.cakeTableDto.order.CreateOrderRequest;
import com.example.demo.entity.cakeTableDto.order.OrderDto;
import com.example.demo.entity.cakeTableDto.order.OrderQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    @GetMapping("/orders")
    public ResponseEntity<PageResponse<OrderDto>> getOrders(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @ModelAttribute OrderQueryRequest queryRequest) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }

            // 设置默认值
            if (queryRequest.getPage() == null || queryRequest.getPage() < 1) {
                queryRequest.setPage(1);
            }
            if (queryRequest.getSize() == null || queryRequest.getSize() < 1) {
                queryRequest.setSize(10);
            }
            if (queryRequest.getSortBy() == null || queryRequest.getSortBy().isEmpty()) {
                queryRequest.setSortBy("createdAt");
            }
            if (queryRequest.getSortOrder() == null || queryRequest.getSortOrder().isEmpty()) {
                queryRequest.setSortOrder("desc");
            }

            PageResponse<OrderDto> orders = orderService.getOrdersByUserIdWithPagination(userId, queryRequest);
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            System.out.println("获取订单列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 分页查询所有订单（管理员接口）
     */
    @GetMapping("/orders/all")
    public ResponseEntity<PageResponse<OrderDto>> getAllOrders(
            @ModelAttribute OrderQueryRequest queryRequest) {
        try {
            // 设置默认值
            if (queryRequest.getPage() == null || queryRequest.getPage() < 1) {
                queryRequest.setPage(1);
            }
            if (queryRequest.getSize() == null || queryRequest.getSize() < 1) {
                queryRequest.setSize(10);
            }
            if (queryRequest.getSortBy() == null || queryRequest.getSortBy().isEmpty()) {
                queryRequest.setSortBy("createdAt");
            }
            if (queryRequest.getSortOrder() == null || queryRequest.getSortOrder().isEmpty()) {
                queryRequest.setSortOrder("desc");
            }

            PageResponse<OrderDto> orders = orderService.getAllOrdersWithPagination(queryRequest);
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            System.out.println("获取订单列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    /**
     * 创建订单
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CreateOrderRequest request) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }

            // 创建订单
            OrderDto order = orderService.createOrder(userId, request);
            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("餐厅不存在")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 根据订单ID获取订单
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {
        try {
            OrderDto order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("订单不存在")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 获取当前用户的订单列表
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<OrderDto>> getMyOrders(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }

            List<OrderDto> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String orderId) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }

            // 取消订单
            OrderDto order = orderService.cancelOrder(orderId, userId);
            return ResponseEntity.ok(order);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("订单不存在")) {
                return ResponseEntity.status(404).body(null);
            } else if (e.getMessage().contains("无权操作此订单")) {
                return ResponseEntity.status(403).body(null);
            } else if (e.getMessage().contains("订单状态不允许取消")) {
                return ResponseEntity.status(400).body(null);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    /**
     * 根据用户ID获取订单列表（管理员接口）
     */
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Integer userId) {
        try {
            List<OrderDto> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}