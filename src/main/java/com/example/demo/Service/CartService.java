package com.example.demo.Service;

import com.example.demo.Repository.CartItemRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.entity.cakeTable.CartItem;
import com.example.demo.entity.cakeTable.Product;
import com.example.demo.entity.cakeTable.Restaurant;
import com.example.demo.entity.cakeTable.User;
import com.example.demo.entity.cakeTableDto.cart.*;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartService {
    
    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    public CartService(CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 获取购物车列表
     */
    public List<CartItemDto> getCartItems(Integer userId) {
        try {
            // 获取用户的购物车项
            List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
            
            // 转换为DTO
            return cartItems.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("获取购物车列表失败，用户ID: {}", userId, e);
            throw new RuntimeException("获取购物车列表失败", e);
        }
    }
    
    /**
     * 添加商品到购物车
     */
    @Transactional
    public CartItemDto addCartItem(Integer userId, CartItemRequestDto requestDto) {
        try {
            if (requestDto.getProductId() == null) {
                throw new IllegalArgumentException("商品ID不能为空");
            }
            
            if (requestDto.getQuantity() == null || requestDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("数量必须大于0");
            }
            
            // 验证商品是否存在且可用
            Product product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
            
            // 检查商品状态
            if (!"available".equals(product.getStatus())) {
                throw new IllegalArgumentException("商品暂不可用");
            }

            // 检查库存
            if (product.getStock() != -1 && product.getStock() < requestDto.getQuantity()) {
                throw new IllegalArgumentException("商品库存不足");
            }

            // 获取餐厅信息
            Restaurant restaurant = product.getRestaurant();
            if (restaurant == null) {
                throw new IllegalArgumentException("商品所属餐厅信息不完整");
            }
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
            // 检查购物车中是否已有该商品
            Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, product.getId());
            
            CartItem cartItem;
            if (existingItem.isPresent()) {
                // 如果已存在，更新数量
                cartItem = existingItem.get();
                Integer newQuantity = cartItem.getQuantity() + requestDto.getQuantity();

                // 再次检查库存
                if (product.getStock() != -1 && product.getStock() < newQuantity) {
                    throw new IllegalArgumentException("商品库存不足，当前购物车已有" + cartItem.getQuantity() + "件");
                }
                
                cartItem.setQuantity(newQuantity);
                cartItem.setSpecifications(requestDto.getSpecifications());
                cartItem.setUpdatedAt(Instant.now());
            } else {
                // 创建新的购物车项
                cartItem = new CartItem();
                cartItem.setRestaurantId(restaurant.getId());
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(requestDto.getQuantity());
                cartItem.setSpecifications(requestDto.getSpecifications());
                cartItem.setCreatedAt(Instant.now());
                cartItem.setUpdatedAt(Instant.now());
            }
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            return convertToDto(savedCartItem);
        } catch (IllegalArgumentException e) {
            log.warn("添加购物车失败，用户ID: {}, 商品ID: {}, 错误信息: {}", userId, requestDto.getProductId(), e.getMessage());
            throw e; // 重新抛出验证异常
        } catch (Exception e) {
            log.error("添加购物车失败，用户ID: {}, 商品ID: {}", userId, requestDto.getProductId(), e);
            throw new RuntimeException("添加购物车失败", e);
        }
    }
    
    /**
     * 更新购物车商品数量
     */
    @Transactional
    public CartItemDto updateCartItemQuantity(Integer userId, Integer itemId, Integer quantity) {
        try {
            // 查找购物车项
            CartItem cartItem = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("购物车项不存在"));
            
            // 验证用户权限
            if (!cartItem.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("无权操作此购物车项");
            }
            
            // 获取商品信息
            Product product = cartItem.getProduct();
            if (product == null) {
                throw new IllegalArgumentException("商品信息不完整");
            }
            
            // 检查库存
            if (product.getStock() != -1 && product.getStock() < quantity) {
                throw new IllegalArgumentException("商品库存不足，仅剩" + product.getStock() + "件");
            }
            
            // 更新数量
            cartItem.setQuantity(quantity);
            cartItem.setUpdatedAt(Instant.now());
            
            CartItem updatedCartItem = cartItemRepository.save(cartItem);
            
            return convertToDto(updatedCartItem);
            
        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出验证异常
        } catch (Exception e) {
            log.error("更新购物车数量失败，用户ID: {}, 购物车项ID: {}", userId, itemId, e);
            throw new RuntimeException("更新购物车数量失败", e);
        }
    }
    
    /**
     * 删除购物车商品
     */
    @Transactional
    public boolean deleteCartItem(Integer userId, Integer itemId) {
        try {
            // 查找购物车项
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
            
            if (cartItemOptional.isEmpty()) {
                return false;
            }
            
            CartItem cartItem = cartItemOptional.get();
            
            // 验证用户权限
            if (!cartItem.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("无权操作此购物车项");
            }
            
            cartItemRepository.delete(cartItem);
            
            return true;
            
        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出验证异常
        } catch (Exception e) {
            log.error("删除购物车项失败，用户ID: {}, 购物车项ID: {}", userId, itemId, e);
            throw new RuntimeException("删除购物车项失败", e);
        }
    }
    
    /**
     * 批量删除购物车商品
     */
    @Transactional
    public int batchDeleteCartItems(Integer userId, List<Integer> itemIds) {
        try {
            // 获取用户的购物车项
            List<CartItem> cartItems = cartItemRepository.findByUserIdAndIdIn(userId, itemIds);
            
            if (cartItems.isEmpty()) {
                return 0;
            }
            
            cartItemRepository.deleteAll(cartItems);
            
            return cartItems.size();
            
        } catch (Exception e) {
            log.error("批量删除购物车项失败，用户ID: {}, 购物车项IDs: {}", userId, itemIds, e);
            throw new RuntimeException("批量删除购物车项失败", e);
        }
    }
    
    /**
     * 清空购物车
     */
    @Transactional
    public void clearCart(Integer userId) {
        try {
            List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
            
            if (!cartItems.isEmpty()) {
                cartItemRepository.deleteAll(cartItems);
            }
            
        } catch (Exception e) {
            log.error("清空购物车失败，用户ID: {}", userId, e);
            throw new RuntimeException("清空购物车失败", e);
        }
    }
    
    /**
     * 获取购物车统计信息
     */
    public CartStatsDto getCartStats(Integer userId) {
        try {
            List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
            
            if (cartItems.isEmpty()) {
                return new CartStatsDto(0, 0, BigDecimal.ZERO, 
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
            }
            
            // 计算总商品数量
            Integer totalItems = cartItems.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            
            // 计算商品种类数
            Integer totalProducts = cartItems.size();
            
            // 计算商品总金额
            BigDecimal totalAmount = cartItems.stream()
                    .map(item -> {
                        BigDecimal price = item.getProduct().getPrice();
                        BigDecimal quantity = new BigDecimal(item.getQuantity());
                        return price.multiply(quantity);
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 获取餐厅数量（去重）
            Set<Integer> restaurantIds = cartItems.stream()
                    .map(CartItem::getRestaurantId)
                    .collect(Collectors.toSet());
            Integer restaurantCount = restaurantIds.size();
            
            // 计算配送费（这里简单处理，实际应根据餐厅和距离计算）
            BigDecimal deliveryFee = BigDecimal.ZERO;
            
            // 计算优惠金额（这里简单处理，实际应有优惠系统）
            BigDecimal discount = BigDecimal.ZERO;
            
            // 计算应付金额
            BigDecimal payableAmount = totalAmount.add(deliveryFee).subtract(discount);
            
            return new CartStatsDto(
                totalItems,
                totalProducts,
                totalAmount,
                deliveryFee,
                discount,
                payableAmount,
                restaurantCount
            );
            
        } catch (Exception e) {
            log.error("获取购物车统计失败，用户ID: {}", userId, e);
            throw new RuntimeException("获取购物车统计失败", e);
        }
    }
    
    /**
     * 转换购物车项为DTO
     */
    private CartItemDto convertToDto(CartItem cartItem) {
        Product product = cartItem.getProduct();
        Restaurant restaurant = product != null ? product.getRestaurant() : null;
        
        // 检查商品是否可用
        Boolean available = true;
        if (product == null) {
            available = false;
        } else if (!"available".equals(product.getStatus())) {
            available = false;
        } else if (product.getStock() != -1 && product.getStock() < cartItem.getQuantity()) {
            available = false;
        }
        
        // 计算总价
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (product != null && product.getPrice() != null) {
            totalPrice = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
        }
        RestaurantDto restaurantDto = null;
        if (restaurant != null) {
            restaurantDto = new RestaurantDto(restaurant);
        }
        return new CartItemDto(
            cartItem.getId(),
            cartItem.getUser().getId(),
            restaurant != null ? restaurantDto : null,
            restaurant != null ? restaurant.getId() : null,
            restaurant != null ? restaurant.getName() : null,
            product != null ? product.getId() : null,
            product != null ? product.getName() : null,
            product != null ? product.getImageUrl() : null,
            product != null ? product.getPrice() : null,
            product != null ? product.getOriginalPrice() : null,
            product != null ? product.getStock() : null,
            cartItem.getQuantity(),
            totalPrice,
            cartItem.getSpecifications(),
            cartItem.getCreatedAt(),
            cartItem.getUpdatedAt(),
            available
        );
    }
}