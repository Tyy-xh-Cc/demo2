package com.example.demo.Service;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductCategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.Product;
import com.example.demo.entity.cakeTable.ProductCategory;
import com.example.demo.entity.cakeTable.Restaurant;
import com.example.demo.entity.cakeTableDto.product.ProductDto;
import com.example.demo.entity.cakeTableDto.restaurant.*;
import com.example.demo.utility.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RestaurantService extends BaseService {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final OrderRepository orderRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository,
                             OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.orderRepository = orderRepository;
    }
    @Transactional
    public RestaurantResponseDto addRestaurant(RestaurantRequestDto requestDto) {
        // 验证餐厅名称是否已存在
        Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(requestDto.getName());
        if (existingRestaurant.isPresent()) {
            throw new IllegalArgumentException("餐厅名称已存在");
        }

        // 创建新的餐厅实体
        Restaurant restaurant = new Restaurant();
        BeanUtils.copyProperties(requestDto, restaurant);

        // 设置默认值
        if (restaurant.getMinOrderAmount() == null) {
            restaurant.setMinOrderAmount(BigDecimal.ZERO);
        }
        if (restaurant.getDeliveryFee() == null) {
            restaurant.setDeliveryFee(BigDecimal.ZERO);
        }
        if (restaurant.getRating() == null) {
            restaurant.setRating(BigDecimal.ZERO);
        }
        if (restaurant.getTotalOrders() == null) {
            restaurant.setTotalOrders(0);
        }
        if (restaurant.getStatus() == null) {
            restaurant.setStatus("closed");
        }
        restaurant.setCreatedAt(Instant.now());

        // 保存餐厅
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // 转换为响应DTO
        return convertToResponseDto(savedRestaurant);
    }
    public List<RestaurantDto> getRecommendedRestaurants(int limit) {
        // 构建分页请求，只获取指定数量的推荐餐厅
        Pageable pageable = buildPageable(new PageRequest(0, limit));

        // 查询推荐餐厅（按照评分和订单量排序，只显示营业中的餐厅）
        List<Restaurant> recommendedRestaurants = restaurantRepository.findRecommendedRestaurants(pageable);

        // 转换为DTO
        return recommendedRestaurants.stream()
                .map(this::convertToRestaurantDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public RestaurantResponseDto updateRestaurant(Integer id, RestaurantRequestDto requestDto) {
        // 查找餐厅
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("餐厅不存在"));

        // 检查名称是否重复（排除当前餐厅）
        if (requestDto.getName() != null && !requestDto.getName().equals(restaurant.getName())) {
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(requestDto.getName());
            if (existingRestaurant.isPresent() && !existingRestaurant.get().getId().equals(id)) {
                throw new IllegalArgumentException("餐厅名称已存在");
            }
        }

        // 更新餐厅信息
        if (requestDto.getName() != null) {
            restaurant.setName(requestDto.getName());
        }
        if (requestDto.getDescription() != null) {
            restaurant.setDescription(requestDto.getDescription());
        }
        if (requestDto.getLogoUrl() != null) {
            restaurant.setLogoUrl(requestDto.getLogoUrl());
        }
        if (requestDto.getCoverUrl() != null) {
            restaurant.setCoverUrl(requestDto.getCoverUrl());
        }
        if (requestDto.getPhone() != null) {
            restaurant.setPhone(requestDto.getPhone());
        }
        if (requestDto.getAddress() != null) {
            restaurant.setAddress(requestDto.getAddress());
        }
        if (requestDto.getOpeningHours() != null) {
            restaurant.setOpeningHours(requestDto.getOpeningHours());
        }
        if (requestDto.getMinOrderAmount() != null) {
            restaurant.setMinOrderAmount(requestDto.getMinOrderAmount());
        }
        if (requestDto.getDeliveryFee() != null) {
            restaurant.setDeliveryFee(requestDto.getDeliveryFee());
        }
        if (requestDto.getEstimatedDeliveryTime() != null) {
            restaurant.setEstimatedDeliveryTime(requestDto.getEstimatedDeliveryTime());
        }
        if (requestDto.getRating() != null) {
            restaurant.setRating(requestDto.getRating());
        }
        if (requestDto.getTotalOrders() != null) {
            restaurant.setTotalOrders(requestDto.getTotalOrders());
        }
        if (requestDto.getStatus() != null) {
            restaurant.setStatus(requestDto.getStatus());
        }

        // 保存更新
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        // 转换为响应DTO
        return convertToResponseDto(updatedRestaurant);
    }

    private RestaurantResponseDto convertToResponseDto(Restaurant restaurant) {
        RestaurantResponseDto responseDto = new RestaurantResponseDto();
        BeanUtils.copyProperties(restaurant, responseDto);
        responseDto.setId(restaurant.getId());
        return responseDto;
    }
    /**
     * 条件分页查询餐厅
     */
    public PageResponse<RestaurantDto> getRestaurantsByConditions(PageRequest pageRequest, RestaurantQueryDto queryDto) {
        // 根据sortBy参数构建排序规则
        Sort sort;
        if (queryDto.getSortBy() != null) {
            sort = switch (queryDto.getSortBy()) {
                case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
                case "orders" -> Sort.by(Sort.Direction.DESC, "totalOrders");
                case "price" -> Sort.by(Sort.Direction.ASC, "minOrderAmount");
                default -> Sort.by(Sort.Direction.DESC, "createdAt");
            };
        } else {
            // 默认按创建时间倒序
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        
        // 构建分页请求，包含排序
        PageRequest customPageRequest = new PageRequest(pageRequest.getPage(), pageRequest.getSize());
        Pageable pageable = buildPageable(customPageRequest, sort);
        
        Page<Restaurant> restaurantPage;

        // 使用复杂条件查询
        restaurantPage = restaurantRepository.findByConditions(
                queryDto.getName(),
                queryDto.getPhone(),
                queryDto.getAddress(),
                queryDto.getStatus(),
                queryDto.getMinRating(),
                queryDto.getMaxRating(),
                queryDto.getKeyword(),
                queryDto.getMinOrderAmount(),
                pageable
        );

        // 将Restaurant转换为RestaurantDto
        List<RestaurantDto> restaurantDtos = restaurantPage.getContent().stream()
                .map(this::convertToRestaurantDto)
                .collect(Collectors.toList());

        return convertToPageResponse(restaurantPage, restaurantDtos);
    }

    /**
     * 将Restaurant实体转换为RestaurantDto
     */
    private RestaurantDto convertToRestaurantDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getLogoUrl(),
                restaurant.getCoverUrl(),
                restaurant.getPhone(),
                restaurant.getAddress(),
                restaurant.getOpeningHours(),
                restaurant.getMinOrderAmount(),
                restaurant.getDeliveryFee(),
                restaurant.getEstimatedDeliveryTime(),
                restaurant.getRating(),
                restaurant.getTotalOrders(),
                restaurant.getStatus(),
                restaurant.getCreatedAt()
        );
    }

    public RestaurantDto getRestaurant(Integer id) {
        try {
            // 1. 查询餐厅基本信息
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
            if (restaurantOptional.isEmpty()) {
                return null;
            }

            Restaurant restaurant = restaurantOptional.get();
            // 6. 构建详情DTO
            return new RestaurantDto(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getDescription(),
                    restaurant.getLogoUrl(),
                    restaurant.getCoverUrl(),
                    restaurant.getPhone(),
                    restaurant.getAddress(),
                    restaurant.getOpeningHours(),
                    restaurant.getMinOrderAmount(),
                    restaurant.getDeliveryFee(),
                    restaurant.getEstimatedDeliveryTime(),
                    restaurant.getRating(),
                    restaurant.getTotalOrders(),
                    restaurant.getStatus(),
                    restaurant.getCreatedAt()
            );

        } catch (Exception e) {
            throw new RuntimeException("获取餐厅详情失败: " + e.getMessage(), e);
        }
    }
    /**
     * 获取餐厅分类列表
     */
    public List<CategorySimpleDto> getRestaurantCategories(Integer restaurantId) {
        try {
            // 验证餐厅是否存在
            if (!restaurantRepository.existsById(restaurantId)) {
                throw new IllegalArgumentException("餐厅不存在");
            }

            // 获取分类列表
            List<ProductCategory> categories = productCategoryRepository.findByRestaurantId(restaurantId);

            return categories.stream()
                    .map(this::convertToSimpleDto)
                    .collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("获取餐厅分类失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取分类详情（包含商品列表）
     */
    public CategoryDetailDto getCategoryDetail(Integer restaurantId, Integer categoryId) {
        try {
            // 验证分类是否存在且属于该餐厅
            Optional<ProductCategory> categoryOptional = productCategoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return null;
            }

            ProductCategory category = categoryOptional.get();
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("分类不属于该餐厅");
            }

            // 获取分类下的商品
            List<Product> products = productRepository.findByCategoryId(categoryId);
            List<ProductDto> productDtos = products.stream()
                    .map(this::convertToProductDto)
                    .collect(Collectors.toList());

            // 获取商品数量
            Integer productCount = productRepository.countByCategoryId(categoryId);

            return new CategoryDetailDto(
                    category.getId(),
                    category.getName(),
                    category.getSortOrder(),
                    productCount != null ? productCount : 0,
                    productDtos,
                    restaurantId,
                    category.getRestaurant().getName()
            );

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("获取分类详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建分类
     */
    @Transactional
    public CategorySimpleDto createCategory(Integer restaurantId, CategoryCreateDto createDto) {
        try {
            // 验证餐厅是否存在
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new IllegalArgumentException("餐厅不存在"));

            // 检查分类名称是否重复
            Optional<ProductCategory> existingCategory = productCategoryRepository
                    .findByRestaurantIdAndName(restaurantId, createDto.getName());
            if (existingCategory.isPresent()) {
                throw new IllegalArgumentException("分类名称已存在");
            }

            // 创建分类
            ProductCategory category = new ProductCategory();
            category.setRestaurant(restaurant);
            category.setName(createDto.getName());
            category.setSortOrder(createDto.getSortOrder() != null ? createDto.getSortOrder() : 0);

            ProductCategory savedCategory = productCategoryRepository.save(category);

            return convertToSimpleDto(savedCategory);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("创建分类失败: " + e.getMessage(), e);
        }
    }

    /**
     * 更新分类
     */
    @Transactional
    public CategorySimpleDto updateCategory(Integer restaurantId, Integer categoryId, CategoryUpdateDto updateDto) {
        try {
            // 查找分类
            Optional<ProductCategory> categoryOptional = productCategoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return null;
            }

            ProductCategory category = categoryOptional.get();

            // 验证分类是否属于该餐厅
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("分类不属于该餐厅");
            }

            // 检查分类名称是否重复（排除当前分类）
            if (updateDto.getName() != null && !updateDto.getName().equals(category.getName())) {
                Optional<ProductCategory> existingCategory = productCategoryRepository
                        .findByRestaurantIdAndName(restaurantId, updateDto.getName());
                if (existingCategory.isPresent() && !existingCategory.get().getId().equals(categoryId)) {
                    throw new IllegalArgumentException("分类名称已存在");
                }
            }

            // 更新字段
            if (updateDto.getName() != null) {
                category.setName(updateDto.getName());
            }
            if (updateDto.getSortOrder() != null) {
                category.setSortOrder(updateDto.getSortOrder());
            }

            ProductCategory updatedCategory = productCategoryRepository.save(category);

            return convertToSimpleDto(updatedCategory);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("更新分类失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除分类
     */
    @Transactional
    public boolean deleteCategory(Integer restaurantId, Integer categoryId) {
        try {
            // 查找分类
            Optional<ProductCategory> categoryOptional = productCategoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return false;
            }

            ProductCategory category = categoryOptional.get();

            // 验证分类是否属于该餐厅
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("分类不属于该餐厅");
            }

            // 检查分类下是否有商品
            Integer productCount = productRepository.countByCategoryId(categoryId);
            if (productCount != null && productCount > 0) {
                throw new IllegalArgumentException("分类下存在商品，无法删除");
            }

            productCategoryRepository.delete(category);

            return true;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("删除分类失败: " + e.getMessage(), e);
        }
    }

    /**
     * 更新分类排序
     */
    @Transactional
    public CategorySimpleDto updateCategorySort(Integer restaurantId, Integer categoryId, Integer sortOrder) {
        try {
            // 查找分类
            Optional<ProductCategory> categoryOptional = productCategoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return null;
            }

            ProductCategory category = categoryOptional.get();

            // 验证分类是否属于该餐厅
            if (!category.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("分类不属于该餐厅");
            }

            // 更新排序
            category.setSortOrder(sortOrder);

            ProductCategory updatedCategory = productCategoryRepository.save(category);

            return convertToSimpleDto(updatedCategory);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("更新分类排序失败: " + e.getMessage(), e);
        }
    }

    /**
     * 转换分类为简单DTO
     */
    private CategorySimpleDto convertToSimpleDto(ProductCategory category) {
        // 查询分类下的商品数量
        Integer productCount = productRepository.countByCategoryId(category.getId());

        return new CategorySimpleDto(
                category.getId(),
                category.getName(),
                productCount != null ? productCount : 0,
                category.getSortOrder()
        );
    }

    /**
     * 转换商品为DTO
     */
    private ProductDto convertToProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getRestaurant().getId(),
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
                product.getUpdatedAt()
        );
    }

}