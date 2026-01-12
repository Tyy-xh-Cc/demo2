package com.example.demo.Repository;


import com.example.demo.entity.cakeTable.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
        Optional<User> findByUsername(String username);
        // 分页查询所有用户
        @NullMarked
        Page<User> findAll(Pageable pageable);
        // 用户名模糊查询
        Page<User> findByUsernameContaining(String username, Pageable pageable);

        // 手机号模糊查询
        Page<User> findByPhoneContaining(String phone, Pageable pageable);

        // 身份精确查询
        Page<User> findByIdentity(String identity, Pageable pageable);

        // 状态精确查询
        Page<User> findByStatus(String status, Pageable pageable);

        // 复杂条件组合查询
        @Query("SELECT u FROM User u WHERE " +
                "(:name IS NULL OR u.username LIKE %:name%) AND " +
                "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
                "(:identity IS NULL OR u.identity = :identity) AND " +
                "(:status IS NULL OR u.status = :status)")
        Page<User> findByConditions(@Param("name") String name,
                                    @Param("phone") String phone,
                                    @Param("identity") String identity,
                                    @Param("status") String status,
                                    Pageable pageable);

}
