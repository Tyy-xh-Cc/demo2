package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    
    // 根据餐厅ID查找分类
    List<ProductCategory> findByRestaurantId(Integer restaurantId);
    
    // 根据餐厅ID和分类名称查找
    Optional<ProductCategory> findByRestaurantIdAndName(Integer restaurantId, String name);
    
    // 根据餐厅ID和分类ID查找
    Optional<ProductCategory> findByRestaurantIdAndId(Integer restaurantId, Integer id);
    
    // 根据餐厅ID按排序查找
    List<ProductCategory> findByRestaurantIdOrderBySortOrderAsc(Integer restaurantId);
    
    // 统计餐厅分类数量
    Integer countByRestaurantId(Integer restaurantId);
}