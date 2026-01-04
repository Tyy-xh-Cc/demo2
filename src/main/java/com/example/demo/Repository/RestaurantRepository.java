package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {
}