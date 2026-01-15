package com.example.demo.Controller;

import com.example.demo.Service.RestaurantService;
import com.example.demo.entity.Dto.LoginResponse;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTableDto.restaurant.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants/recommended")
    public ResponseEntity<List<RestaurantDto>> getRecommendedRestaurants(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<RestaurantDto> recommendedRestaurants = restaurantService.getRecommendedRestaurants(limit);
            return ResponseEntity.ok(recommendedRestaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantResponseDto> addRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        try {
            RestaurantResponseDto responseDto = restaurantService.addRestaurant(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @PathVariable Integer id,
            @Valid @RequestBody RestaurantRequestDto requestDto) {
        try {
            RestaurantResponseDto responseDto = restaurantService.updateRestaurant(id, requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<?> getRestaurantMenu(@PathVariable Integer id) {
        try {
            RestaurantDto restaurantDto  = restaurantService.getRestaurant(id);

            if (restaurantDto == null) {
                return ResponseEntity.status(404)
                        .body(new LoginResponse<>(false, "餐厅不存在"));
            }

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "获取成功", null, restaurantDto));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取餐厅菜单失败: " + e.getMessage()));
        }
    }
    @GetMapping("/paged/restaurants")
    public PageResponse<RestaurantDto> getPagedRestaurants(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort_by,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal min_order_amount,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {

        PageRequest pageRequest = new PageRequest(page, size);
        RestaurantQueryDto queryDto = new RestaurantQueryDto(name, phone, address, status, minRating, maxRating, keyword, sort_by, min_order_amount);

        return restaurantService.getRestaurantsByConditions(pageRequest, queryDto);
    }
    /**
     * 获取餐厅分类
     * GET /api/restaurants/{restaurantId}/categories
     */
    @GetMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<?> getRestaurantCategories(@PathVariable Integer restaurantId) {
        try {
            List<CategorySimpleDto> categories = restaurantService.getRestaurantCategories(restaurantId);
            if (categories == null || categories.isEmpty()) {
                return ResponseEntity.ok()
                        .body(new LoginResponse<>(true, "该餐厅暂无分类", null, List.of()));
            }

            return ResponseEntity.ok()
                    .body(categories);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取餐厅分类失败: " + e.getMessage()));
        }
    }

    /**
     * 获取餐厅分类详情（包含商品列表）
     * GET /api/restaurants/{restaurantId}/categories/{categoryId}
     */
    @GetMapping("/restaurants/{restaurantId}/categories/{categoryId}")
    public ResponseEntity<?> getCategoryDetail(
            @PathVariable Integer restaurantId,
            @PathVariable Integer categoryId) {
        try {
            CategoryDetailDto categoryDetail = restaurantService.getCategoryDetail(restaurantId, categoryId);

            if (categoryDetail == null) {
                return ResponseEntity.status(404)
                        .body(new LoginResponse<>(false, "分类不存在"));
            }

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "获取成功", null, categoryDetail));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取分类详情失败: " + e.getMessage()));
        }
    }

    /**
     * 创建餐厅分类
     * POST /api/restaurants/{restaurantId}/categories
     */
    @PostMapping("/restaurants/{restaurantId}/categories")
    public ResponseEntity<?> createCategory(
            @PathVariable Integer restaurantId,
            @Valid @RequestBody CategoryCreateDto createDto) {
        try {
            CategorySimpleDto category = restaurantService.createCategory(restaurantId, createDto);

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "创建成功", null, category));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "创建分类失败: " + e.getMessage()));
        }
    }

    /**
     * 更新餐厅分类
     * PUT /api/restaurants/{restaurantId}/categories/{categoryId}
     */
    @PutMapping("/restaurants/{restaurantId}/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Integer restaurantId,
            @PathVariable Integer categoryId,
            @Valid @RequestBody CategoryUpdateDto updateDto) {
        try {
            CategorySimpleDto category = restaurantService.updateCategory(restaurantId, categoryId, updateDto);

            if (category == null) {
                return ResponseEntity.status(404)
                        .body(new LoginResponse<>(false, "分类不存在"));
            }

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "更新成功", null, category));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "更新分类失败: " + e.getMessage()));
        }
    }

    /**
     * 删除餐厅分类
     * DELETE /api/restaurants/{restaurantId}/categories/{categoryId}
     */
    @DeleteMapping("/restaurants/{restaurantId}/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Integer restaurantId,
            @PathVariable Integer categoryId) {
        try {
            boolean deleted = restaurantService.deleteCategory(restaurantId, categoryId);

            if (!deleted) {
                return ResponseEntity.status(404)
                        .body(new LoginResponse<>(false, "分类不存在"));
            }

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "删除成功"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "删除分类失败: " + e.getMessage()));
        }
    }

    /**
     * 更新分类排序
     * PUT /api/restaurants/{restaurantId}/categories/{categoryId}/sort
     */
    @PutMapping("/restaurants/{restaurantId}/categories/{categoryId}/sort")
    public ResponseEntity<?> updateCategorySort(
            @PathVariable Integer restaurantId,
            @PathVariable Integer categoryId,
            @RequestParam Integer sortOrder) {
        try {
            CategorySimpleDto category = restaurantService.updateCategorySort(restaurantId, categoryId, sortOrder);

            if (category == null) {
                return ResponseEntity.status(404)
                        .body(new LoginResponse<>(false, "分类不存在"));
            }

            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "排序更新成功", null, category));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "更新排序失败: " + e.getMessage()));
        }
    }
}