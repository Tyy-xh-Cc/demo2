package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.DeliveryPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Integer>, JpaSpecificationExecutor<DeliveryPerson> {
    /**
     * 根据配送员姓名查找
     */
    Optional<DeliveryPerson> findByName(String name);

    /**
     * 复杂条件查询配送员
     */
    @Query("SELECT d FROM DeliveryPerson d WHERE " +
            "(:name IS NULL OR d.name LIKE %:name%) AND " +
            "(:phone IS NULL OR d.phone LIKE %:phone%) AND " +
            "(:status IS NULL OR d.status = :status) AND " +
            "(:vehicleType IS NULL OR d.vehicleType LIKE %:vehicleType%) AND " +
            "(:minRating IS NULL OR d.rating >= :minRating) AND " +
            "(:maxRating IS NULL OR d.rating <= :maxRating)")
    Page<DeliveryPerson> findByConditions(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("status") String status,
            @Param("vehicleType") String vehicleType,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            Pageable pageable);
}