package com.example.demo.Service;

import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;

import com.example.demo.entity.cakeTable.Restaurant;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantQueryDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantRequestDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantResponseDto;
import com.example.demo.utility.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RestaurantService extends BaseService {
    
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }
    @Transactional
    public RestaurantResponseDto addRestaurant(RestaurantRequestDto requestDto) {
        // 验证餐厅名称是否已存在
        Optional<Restaurant> existingRestaurant = repository.findByName(requestDto.getName());
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
        Restaurant savedRestaurant = repository.save(restaurant);

        // 转换为响应DTO
        return convertToResponseDto(savedRestaurant);
    }
    public List<RestaurantDto> getRecommendedRestaurants(int limit) {
        // 构建分页请求，只获取指定数量的推荐餐厅
        Pageable pageable = buildPageable(new PageRequest(0, limit));

        // 查询推荐餐厅（按照评分和订单量排序，只显示营业中的餐厅）
        List<Restaurant> recommendedRestaurants = repository.findRecommendedRestaurants(pageable);

        // 转换为DTO
        return recommendedRestaurants.stream()
                .map(this::convertToRestaurantDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public RestaurantResponseDto updateRestaurant(Integer id, RestaurantRequestDto requestDto) {
        // 查找餐厅
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("餐厅不存在"));

        // 检查名称是否重复（排除当前餐厅）
        if (requestDto.getName() != null && !requestDto.getName().equals(restaurant.getName())) {
            Optional<Restaurant> existingRestaurant = repository.findByName(requestDto.getName());
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
        Restaurant updatedRestaurant = repository.save(restaurant);

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
        Pageable pageable = buildPageable(pageRequest);
        Page<Restaurant> restaurantPage;

        // 根据查询条件选择不同的查询方法
        if (queryDto.isEmpty()) {
            // 如果没有查询条件，查询所有餐厅
            restaurantPage = repository.findAll(pageable);
        } else {
            // 使用复杂条件查询
            restaurantPage = repository.findByConditions(
                    queryDto.getName(),
                    queryDto.getPhone(),
                    queryDto.getAddress(),
                    queryDto.getStatus(),
                    queryDto.getMinRating(),
                    queryDto.getMaxRating(),
                    pageable
            );
        }

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
}