package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.CartItem;
import com.example.demo.entity.cakeTable.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
    // 根据用户ID查找购物车项
    List<CartItem> findByUserId(Integer userId);
    
    // 根据用户ID和商品ID查找购物车项
    Optional<CartItem> findByUserIdAndProductId(Integer userId, Integer productId);
    
    // 根据用户ID和商品ID列表查找购物车项
    List<CartItem> findByUserIdAndIdIn(Integer userId, List<Integer> ids);
    
    // 统计用户购物车商品数量
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user.id = :userId")
    Integer countItemsByUserId(@Param("userId") Integer userId);
    
    // 统计用户购物车商品种类数
    @Query("SELECT COUNT(DISTINCT ci.product.id) FROM CartItem ci WHERE ci.user.id = :userId")
    Integer countProductsByUserId(@Param("userId") Integer userId);
    
    // 删除用户的购物车项
    void deleteByUserId(Integer userId);
}