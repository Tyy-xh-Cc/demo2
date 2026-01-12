package com.example.demo.Repository;

import com.example.demo.entity.cakeTable.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {
    
    // 根据状态查找轮播图
    List<Banner> findByStatus(String status);
    
    // 根据状态和排序查找
    List<Banner> findByStatusOrderBySortOrderAsc(String status);
    
    // 查找当前有效的轮播图（状态为active且在有效期内）
    @Query("SELECT b FROM Banner b WHERE b.status = 'active' " +
           "AND (b.startTime IS NULL OR b.startTime <= :now) " +
           "AND (b.endTime IS NULL OR b.endTime >= :now) " +
           "ORDER BY b.sortOrder ASC, b.createdAt DESC")
    List<Banner> findActiveBanners(@Param("now") Instant now);
    
    // 分页查询
    Page<Banner> findAll(Pageable pageable);
    
    // 根据状态分页查询
    Page<Banner> findByStatus(String status, Pageable pageable);
    
    // 根据标题模糊查询
    Page<Banner> findByTitleContaining(String title, Pageable pageable);
    
    // 复合条件查询
    @Query("SELECT b FROM Banner b WHERE " +
           "(:title IS NULL OR b.title LIKE %:title%) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:linkType IS NULL OR b.linkType = :linkType)")
    Page<Banner> findByConditions(@Param("title") String title,
                                 @Param("status") String status,
                                 @Param("linkType") String linkType,
                                 Pageable pageable);
}