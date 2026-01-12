package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Address;
import com.example.demo.entity.cakeTable.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> , JpaSpecificationExecutor<Address> {
  Long countByUser(User user);

  // 获取用户的地址列表
  List<Address> findByUser(User user);
}