package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.CartItem;
import com.example.demo.entity.cakeTable.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>, JpaSpecificationExecutor<CartItem> {

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.user = :user")
    Long sumQuantityByUser(@Param("user") User user);

    // 获取用户的购物车商品
    List<CartItem> findByUser(User user);
}