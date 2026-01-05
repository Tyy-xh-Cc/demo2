package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends JpaRepository<Address, Integer> , JpaSpecificationExecutor<Address> {
  }