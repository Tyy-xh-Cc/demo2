// ProductController.java
package com.example.demo.Controller;

import com.example.demo.Service.ProductService;
import com.example.demo.entity.Dto.LoginResponse;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTableDto.product.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * 分页查询产品列表
     * 对应前端的 getProductList(params) 方法
     */
    @GetMapping("/products")
    public PageResponse<ProductDto> getPagedProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer restaurantId,
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) String status) {
        
        PageRequest pageRequest = new PageRequest(page, size);
        ProductQueryDto queryDto = new ProductQueryDto(name, restaurantId, restaurantName, 
                categoryId, minPrice, maxPrice, minStock, maxStock, status);
        
        return productService.getProductsByConditions(pageRequest, queryDto);
    }
    
    /**
     * 根据ID获取产品详情
     */
    @GetMapping("/products/{id}")
    public LoginResponse<?> getProduct(@PathVariable Integer id) {
        ProductResponseDto responseDto = productService.getProductById(id);
        if (responseDto.isSuccess()) {
            return new LoginResponse<>(true, responseDto.getMessage(), null, responseDto);
        } else {
            return new LoginResponse<>(false, responseDto.getMessage());
        }
    }
    
    /**
     * 创建新产品
     */
    @PostMapping("/products")
    public LoginResponse<?> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        try {
            System.out.println(requestDto.toString());
            ProductResponseDto responseDto = productService.createProduct(requestDto);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "创建产品失败");
        }
    }
    
    /**
     * 更新产品信息
     */
    @PutMapping("/products/{id}")
    public LoginResponse<?> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequestDto requestDto) {
        try {
            ProductResponseDto responseDto = productService.updateProduct(id, requestDto);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "更新产品失败");
        }
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public LoginResponse<?> deleteProduct(@PathVariable Integer id) {
        try {
            ProductResponseDto responseDto = productService.deleteProduct(id);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "删除产品失败");
        }
    }
    
    /**
     * 更新产品状态
     */
    @PutMapping("/products/{id}/status")
    public LoginResponse<?> updateProductStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        try {
            ProductResponseDto responseDto = productService.updateProductStatus(id, status);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "更新产品状态失败");
        }
    }
    
    /**
     * 更新产品库存
     */
    @PutMapping("/products/{id}/stock")
    public LoginResponse<?> updateProductStock(
            @PathVariable Integer id,
            @RequestParam Integer stock) {
        try {
            ProductResponseDto responseDto = productService.updateProductStock(id, stock);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "更新产品库存失败");
        }
    }
    
    /**
     * 根据餐厅ID获取产品列表（不分页）
     */
    @GetMapping("/restaurants/{restaurantId}/products")
    public LoginResponse<?> getProductsByRestaurant(@PathVariable Integer restaurantId) {
        try {
            List<ProductDto> products = productService.getProductsByRestaurantId(restaurantId);
            return new LoginResponse<>(true, "获取成功", null, products);
        } catch (Exception e) {
            return new LoginResponse<>(false, "获取产品列表失败");
        }
    }
    
    /**
     * 根据分类ID获取产品列表（不分页）
     */
    @GetMapping("/categories/{categoryId}/products")
    public LoginResponse<?> getProductsByCategory(@PathVariable Integer categoryId) {
        try {
            List<ProductDto> products = productService.getProductsByCategoryId(categoryId);
            return new LoginResponse<>(true, "获取成功", null, products);
        } catch (Exception e) {
            return new LoginResponse<>(false, "获取产品列表失败");
        }
    }
    
    /**
     * 根据状态获取产品列表（不分页）
     */
    @GetMapping("/products/status/{status}")
    public LoginResponse<?> getProductsByStatus(@PathVariable String status) {
        try {
            List<ProductDto> products = productService.getProductsByStatus(status);
            return new LoginResponse<>(true, "获取成功", null, products);
        } catch (Exception e) {
            return new LoginResponse<>(false, "获取产品列表失败");
        }
    }
}