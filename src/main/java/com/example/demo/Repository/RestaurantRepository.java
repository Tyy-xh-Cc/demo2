package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {
    Page<Restaurant> findAll(Pageable pageable);

    Optional<Restaurant> findByName(String name);
    // 复杂条件组合查询
    @Query("SELECT r FROM Restaurant r WHERE " +
            "(:name IS NULL OR r.name LIKE %:name%) AND " +
            "(:phone IS NULL OR r.phone LIKE %:phone%) AND " +
            "(:address IS NULL OR r.address LIKE %:address%) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(:minRating IS NULL OR r.rating >= :minRating) AND " +
            "(:maxRating IS NULL OR r.rating <= :maxRating) AND " +
            "(:keyword IS NULL OR r.name LIKE %:keyword% OR r.description LIKE %:keyword%) AND " +
            "(:minOrderAmount IS NULL OR r.minOrderAmount >= :minOrderAmount)")
    Page<Restaurant> findByConditions(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("address") String address,
            @Param("status") String status,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            @Param("keyword") String keyword,
            @Param("minOrderAmount") BigDecimal minOrderAmount,
            Pageable pageable);
    @Query("SELECT r FROM Restaurant r WHERE r.status = 'open' " +
            "ORDER BY r.rating DESC, r.totalOrders DESC")
    List<Restaurant> findRecommendedRestaurants(Pageable pageable);
}
