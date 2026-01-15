package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Address;
import com.example.demo.entity.cakeTable.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> , JpaSpecificationExecutor<Address> {
  Long countByUser(User user);
  List<Address> findByUserId(Integer userId);
  // 获取用户的地址列表
  List<Address> findByUser(User user);
  // 根据用户ID和是否默认查找地址
  Optional<Address> findByUserIdAndIsDefault(Integer userId, Boolean isDefault);

  // 根据用户ID查找默认地址
  @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = true")
  Optional<Address> findDefaultAddressByUserId(@Param("userId") Integer userId);
  @Modifying
  @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
  void unsetAllDefaultAddressesByUserId(@Param("userId") Integer userId);

  // 设置指定地址为默认地址
  @Modifying
  @Query("UPDATE Address a SET a.isDefault = true WHERE a.id = :addressId")
  void setAddressAsDefault(@Param("addressId") Integer addressId);

}