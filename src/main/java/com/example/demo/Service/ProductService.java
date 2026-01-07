// ProductService.java
package com.example.demo.Service;

import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.Product;
import com.example.demo.entity.cakeTable.Restaurant;

import com.example.demo.entity.cakeTableDto.product.ProductDto;
import com.example.demo.entity.cakeTableDto.product.ProductQueryDto;
import com.example.demo.entity.cakeTableDto.product.ProductRequestDto;
import com.example.demo.entity.cakeTableDto.product.ProductResponseDto;
import com.example.demo.utility.BaseService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService extends BaseService {
    
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    
    public ProductService(ProductRepository productRepository, RestaurantRepository restaurantRepository) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
    }
    
    /**
     * 分页查询产品列表
     */
    public PageResponse<ProductDto> getProductsByConditions(PageRequest pageRequest, ProductQueryDto queryDto) {
        Pageable pageable = buildPageable(pageRequest);
        Page<Product> productPage;
        
        if (queryDto.isEmpty()) {
            // 无条件查询
            productPage = productRepository.findAll(pageable);
        } else {
            // 条件查询
            productPage = productRepository.findByConditions(
                    queryDto.getName(),
                    queryDto.getRestaurantId(),
                    queryDto.getCategoryId(),
                    queryDto.getMinPrice(),
                    queryDto.getMaxPrice(),
                    queryDto.getMinStock(),
                    queryDto.getMaxStock(),
                    queryDto.getStatus(),
                    pageable
            );
        }
        
        // 转换为DTO
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return convertToPageResponse(productPage, productDtos);
    }
    

    public ProductResponseDto getProductById(Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return new ProductResponseDto(null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null,
                    false, "产品不存在");
        }
        
        Product product = productOptional.get();
        return convertToResponseDto(product, true, "获取成功");
    }
    
    /**
     * 创建新产品
     */
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        try {
            // 验证必填字段
            if (requestDto.getRestaurantId() == null) {
                return createErrorResponse("餐厅ID不能为空");
            }
            
            if (requestDto.getName() == null || requestDto.getName().trim().isEmpty()) {
                return createErrorResponse("产品名称不能为空");
            }
            
            if (requestDto.getPrice() == null) {
                return createErrorResponse("产品价格不能为空");
            }
            
            // 验证餐厅是否存在
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(requestDto.getRestaurantId());
            if (restaurantOptional.isEmpty()) {
                return createErrorResponse("餐厅不存在");
            }
            
            // 检查同一餐厅下是否存在同名产品
            Optional<Product> existingProduct = productRepository.findByRestaurantIdAndName(
                    requestDto.getRestaurantId(), requestDto.getName());
            if (existingProduct.isPresent()) {
                return createErrorResponse("该餐厅下已存在同名产品");
            }
            
            // 创建新产品
            Product product = new Product();
            product.setRestaurant(restaurantOptional.get());
            product.setName(requestDto.getName().trim());
            product.setPrice(requestDto.getPrice());
            
            // 设置可选字段
            if (requestDto.getCategoryId() != null) {
                product.setCategoryId(requestDto.getCategoryId());
            }
            if (requestDto.getDescription() != null) {
                product.setDescription(requestDto.getDescription().trim());
            }
            if (requestDto.getImageUrl() != null) {
                product.setImageUrl(requestDto.getImageUrl().trim());
            }
            if (requestDto.getOriginalPrice() != null) {
                product.setOriginalPrice(requestDto.getOriginalPrice());
            }
            
            // 设置默认值
            product.setStock(requestDto.getStock() != null ? requestDto.getStock() : -1);
            product.setSalesCount(requestDto.getSalesCount() != null ? requestDto.getSalesCount() : 0);
            product.setSortOrder(requestDto.getSortOrder() != null ? requestDto.getSortOrder() : 0);
            product.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "available");
            product.setCreatedAt(Instant.now());
            product.setUpdatedAt(Instant.now());
            
            // 保存产品
            Product savedProduct = productRepository.save(product);
            
            return convertToResponseDto(savedProduct, true, "产品创建成功");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("创建产品失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新产品信息
     */
    @Transactional
    public ProductResponseDto updateProduct(Integer id, ProductRequestDto requestDto) {
        try {
            // 检查产品是否存在
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                return createErrorResponse("产品不存在");
            }
            
            Product product = productOptional.get();
            
            // 验证餐厅ID（如果要修改）
            if (requestDto.getRestaurantId() != null && 
                !requestDto.getRestaurantId().equals(product.getRestaurant().getId())) {
                Optional<Restaurant> restaurantOptional = restaurantRepository.findById(requestDto.getRestaurantId());
                if (restaurantOptional.isEmpty()) {
                    return createErrorResponse("餐厅不存在");
                }
                product.setRestaurant(restaurantOptional.get());
            }
            
            // 检查同一餐厅下是否存在同名产品（排除当前产品）
            if (requestDto.getName() != null && !requestDto.getName().trim().isEmpty()) {
                Optional<Product> existingProduct = productRepository.findByRestaurantIdAndName(
                        product.getRestaurant().getId(), requestDto.getName());
                if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
                    return createErrorResponse("该餐厅下已存在同名产品");
                }
            }
            
            // 更新字段（只更新非空字段）
            if (requestDto.getName() != null) {
                product.setName(requestDto.getName().trim());
            }
            if (requestDto.getCategoryId() != null) {
                product.setCategoryId(requestDto.getCategoryId());
            }
            if (requestDto.getDescription() != null) {
                product.setDescription(requestDto.getDescription());
            }
            if (requestDto.getImageUrl() != null) {
                product.setImageUrl(requestDto.getImageUrl());
            }
            if (requestDto.getPrice() != null) {
                product.setPrice(requestDto.getPrice());
            }
            if (requestDto.getOriginalPrice() != null) {
                product.setOriginalPrice(requestDto.getOriginalPrice());
            }
            if (requestDto.getStock() != null) {
                product.setStock(requestDto.getStock());
            }
            if (requestDto.getSalesCount() != null) {
                product.setSalesCount(requestDto.getSalesCount());
            }
            if (requestDto.getSortOrder() != null) {
                product.setSortOrder(requestDto.getSortOrder());
            }
            if (requestDto.getStatus() != null) {
                product.setStatus(requestDto.getStatus());
            }
            
            product.setUpdatedAt(Instant.now());
            
            // 保存更新
            Product updatedProduct = productRepository.save(product);
            
            return convertToResponseDto(updatedProduct, true, "产品更新成功");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("更新产品失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除产品
     */
    @Transactional
    public ProductResponseDto deleteProduct(Integer id) {
        try {
            // 检查产品是否存在
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                return createErrorResponse("产品不存在");
            }
            
            Product product = productOptional.get();
            
            // 检查产品是否已被使用（例如在购物车中）
            if (!product.getCartItems().isEmpty()) {
                return createErrorResponse("该产品已被添加到购物车，无法删除");
            }
            
            // 执行删除
            productRepository.delete(product);
            
            return new ProductResponseDto(id, null, null, null, null, null, null, 
                     null, null, null, null, null, null, null, null,
                    true, "产品删除成功");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("删除产品失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新产品状态
     */
    @Transactional
    public ProductResponseDto updateProductStatus(Integer id, String status) {
        try {
            // 检查产品是否存在
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                return createErrorResponse("产品不存在");
            }
            
            Product product = productOptional.get();
            
            // 验证状态
            if (!isValidStatus(status)) {
                return createErrorResponse("无效的产品状态");
            }
            
            // 更新状态
            product.setStatus(status);
            product.setUpdatedAt(Instant.now());
            
            Product updatedProduct = productRepository.save(product);
            
            return convertToResponseDto(updatedProduct, true, "产品状态更新成功");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("更新产品状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新产品库存
     */
    @Transactional
    public ProductResponseDto updateProductStock(Integer id, Integer stock) {
        try {
            // 检查产品是否存在
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) {
                return createErrorResponse("产品不存在");
            }
            
            Product product = productOptional.get();
            
            // 更新库存
            product.setStock(stock);
            product.setUpdatedAt(Instant.now());
            
            Product updatedProduct = productRepository.save(product);
            
            return convertToResponseDto(updatedProduct, true, "产品库存更新成功");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("更新产品库存失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据餐厅ID获取产品列表
     */
    public List<ProductDto> getProductsByRestaurantId(Integer restaurantId) {
        List<Product> products = productRepository.findByRestaurantId(restaurantId);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据分类ID获取产品列表
     */
    public List<ProductDto> getProductsByCategoryId(Integer categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据状态获取产品列表
     */
    public List<ProductDto> getProductsByStatus(String status) {
        List<Product> products = productRepository.findByStatus(status);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // 辅助方法
    private ProductDto convertToDto(Product product) {
        String restaurantName = product.getRestaurant() != null ? product.getRestaurant().getName() : null;
        
        return new ProductDto(
                product.getId(),
                product.getRestaurant() != null ? product.getRestaurant().getId() : null,
                restaurantName,
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getStock(),
                product.getSalesCount(),
                product.getSortOrder(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
    
    private ProductResponseDto convertToResponseDto(Product product, boolean success, String message) {
        String restaurantName = product.getRestaurant() != null ? product.getRestaurant().getName() : null;
        
        return new ProductResponseDto(
                product.getId(),
                product.getRestaurant() != null ? product.getRestaurant().getId() : null,
                restaurantName,
                product.getCategoryId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getStock(),
                product.getSalesCount(),
                product.getSortOrder(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                success,
                message
        );
    }
    
    private ProductResponseDto createErrorResponse(String message) {
        return new ProductResponseDto(null, null, null, null, null, null, null, 
                 null, null, null, null, null, null, null, null,
                false, message);
    }
    
    private boolean isValidStatus(String status) {
        return List.of("available", "unavailable", "out_of_stock", "deleted").contains(status);
    }
}