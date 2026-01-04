package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer>, JpaSpecificationExecutor<Delivery> {
}