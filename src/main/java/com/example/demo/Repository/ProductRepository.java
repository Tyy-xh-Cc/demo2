package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findByRestaurantId(Integer restaurantId);

    // 根据分类ID查询产品
    List<Product> findByCategoryId(Integer categoryId);

    // 根据名称模糊查询
    List<Product> findByNameContaining(String name);

    // 根据状态查询
    List<Product> findByStatus(String status);

    // 根据餐厅ID和状态查询
    List<Product> findByRestaurantIdAndStatus(Integer restaurantId, String status);

    // 条件分页查询
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:restaurantId IS NULL OR p.restaurant.id = :restaurantId) AND " +
            "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:minStock IS NULL OR p.stock >= :minStock) AND " +
            "(:maxStock IS NULL OR p.stock <= :maxStock) AND " +
            "(:status IS NULL OR p.status = :status)")
    Page<Product> findByConditions(
            @Param("name") String name,
            @Param("restaurantId") Integer restaurantId,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minStock") Integer minStock,
            @Param("maxStock") Integer maxStock,
            @Param("status") String status,
            Pageable pageable);

    // 根据餐厅名称模糊查询
    @Query("SELECT p FROM Product p WHERE p.restaurant.name LIKE %:restaurantName%")
    List<Product> findByRestaurantNameContaining(@Param("restaurantName") String restaurantName);

    // 检查同一餐厅下是否存在同名产品
    Optional<Product> findByRestaurantIdAndName(Integer restaurantId, String name);

    Integer countByRestaurantId(Integer id);
    
    // 根据分类ID和状态查找产品
    List<Product> findByCategoryIdAndStatus(Integer categoryId, String status);

    // 根据餐厅ID和分类ID查找产品
    List<Product> findByRestaurantIdAndCategoryId(Integer restaurantId, Integer categoryId);

    Integer countByCategoryId(Integer categoryId);
}