package com.example.demo.Controller;

import com.example.demo.Service.CartService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.LoginResponse;
import com.example.demo.entity.cakeTableDto.cart.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    private final UserService userService;
    
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }
    
    /**
     * 获取购物车列表
     * GET /api/cart/items
     */
    @GetMapping("/items")
    public ResponseEntity<?> getCartItems(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        try {
            List<CartItemDto> cartItems = cartService.getCartItems(userId);
            
            // 计算购物车统计信息
            CartStatsDto stats = cartService.getCartStats(userId);
            
            CartResponse response = new CartResponse(
                true,
                "获取购物车成功",
                cartItems,
                stats
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取购物车失败: " + e.getMessage()));
        }
    }
    
    /**
     * 添加商品到购物车
     * POST /api/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<?> addCartItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CartItemRequestDto requestDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            CartItemDto cartItem = cartService.addCartItem(userId, requestDto);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "添加成功", null, cartItem));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "添加购物车失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新购物车商品数量
     * PUT /api/cart/items/{itemId}
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer itemId,
            @RequestParam Integer quantity) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "数量必须大于0"));
            }
            
            CartItemDto cartItem = cartService.updateCartItemQuantity(userId, itemId, quantity);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "更新成功", null, cartItem));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "更新购物车失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除购物车商品
     * DELETE /api/cart/items/{itemId}
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteCartItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Integer itemId) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            boolean deleted = cartService.deleteCartItem(userId, itemId);
            
            if (deleted) {
                return ResponseEntity.ok()
                        .body(new LoginResponse<>(true, "删除成功"));
            } else {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "购物车项不存在"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "删除购物车失败: " + e.getMessage()));
        }
    }
    
    /**
     * 清空购物车
     * DELETE /api/cart/clear
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            cartService.clearCart(userId);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "购物车已清空"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "清空购物车失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取购物车统计信息
     * GET /api/cart/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getCartStats(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            CartStatsDto stats = cartService.getCartStats(userId);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "获取成功", null, stats));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取购物车统计失败: " + e.getMessage()));
        }
    }
    
    /**
     * 批量删除购物车商品
     * POST /api/cart/batch-delete
     */
    @PostMapping("/batch-delete")
    public ResponseEntity<?> batchDeleteCartItems(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody List<Integer> itemIds) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "未授权访问，请先登录"));
        }
        
        String token = authHeader.substring(7);
        Integer userId = userService.getUserByToken(token).getId();
        
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse<>(false, "Token无效或已过期"));
        }
        
        try {
            if (itemIds == null || itemIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "请选择要删除的商品"));
            }
            
            int deletedCount = cartService.batchDeleteCartItems(userId, itemIds);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, 
                            String.format("成功删除%d个商品", deletedCount)));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "批量删除失败: " + e.getMessage()));
        }
    }
}