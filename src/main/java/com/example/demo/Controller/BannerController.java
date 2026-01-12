package com.example.demo.Controller;

import com.example.demo.Service.BannerService;
import com.example.demo.entity.Dto.LoginResponse;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.cakeTableDto.banners.BannerDto;
import com.example.demo.entity.Dto.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/common")
@CrossOrigin(origins = "*")
public class BannerController {
    
    private final BannerService bannerService;
    
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }
    
    /**
     * 获取轮播图列表（前端首页调用）
     * GET /api/common/banners
     */
    @GetMapping("/banners")
    public ResponseEntity<?> getBanners(@RequestParam(required = false) String position) {
        try {
            List<BannerDto> banners = bannerService.getActiveBanners();
            
            if (banners.isEmpty()) {
                return ResponseEntity.ok().body(new LoginResponse<>(true, "暂无轮播图", null, List.of()));
            }
            
            return ResponseEntity.ok().body(new LoginResponse<>(true, "获取成功", null, banners));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取轮播图失败: " + e.getMessage()));
        }
    }
    
    /**
     * 分页查询轮播图（后台管理用）
     * GET /api/common/banners/paged
     */
    @GetMapping("/banners/paged")
    public ResponseEntity<PageResponse<BannerDto>> getPagedBanners(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String linkType) {
        
        PageRequest pageRequest = new PageRequest(page, size);
        PageResponse<BannerDto> response = bannerService.getBannersByConditions(pageRequest, title, status, linkType);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取轮播图详情
     * GET /api/common/banners/{id}
     */
    @GetMapping("/banners/{id}")
    public ResponseEntity<?> getBannerById(@PathVariable Integer id) {
        try {
            BannerDto bannerDto = bannerService.getBannerById(id);
            
            if (bannerDto == null) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "轮播图不存在"));
            }
            
            return ResponseEntity.ok().body(new LoginResponse<>(true, "获取成功", null, bannerDto));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取轮播图详情失败: " + e.getMessage()));
        }
    }
    
    /**
     * 创建轮播图
     * POST /api/common/banners
     */
    @PostMapping("/banners")
    public ResponseEntity<?> createBanner(@RequestBody BannerDto bannerDto) {
        try {
            BannerDto createdBanner = bannerService.createBanner(bannerDto);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "创建成功", null, createdBanner));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "创建轮播图失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新轮播图
     * PUT /api/common/banners/{id}
     */
    @PutMapping("/banners/{id}")
    public ResponseEntity<?> updateBanner(@PathVariable Integer id, @RequestBody BannerDto bannerDto) {
        try {
            BannerDto updatedBanner = bannerService.updateBanner(id, bannerDto);
            
            if (updatedBanner == null) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "轮播图不存在"));
            }
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "更新成功", null, updatedBanner));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "更新轮播图失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除轮播图
     * DELETE /api/common/banners/{id}
     */
    @DeleteMapping("/banners/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Integer id) {
        try {
            boolean deleted = bannerService.deleteBanner(id);
            
            if (!deleted) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "轮播图不存在"));
            }
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "删除成功"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "删除轮播图失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新轮播图状态
     * PUT /api/common/banners/{id}/status
     */
    @PutMapping("/banners/{id}/status")
    public ResponseEntity<?> updateBannerStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            BannerDto updatedBanner = bannerService.updateBannerStatus(id, status);
            
            if (updatedBanner == null) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "轮播图不存在"));
            }
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "状态更新成功", null, updatedBanner));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "更新状态失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据状态获取轮播图
     * GET /api/common/banners/status/{status}
     */
    @GetMapping("/banners/status/{status}")
    public ResponseEntity<?> getBannersByStatus(@PathVariable String status) {
        try {
            List<BannerDto> banners = bannerService.getBannersByStatus(status);
            
            return ResponseEntity.ok()
                    .body(new LoginResponse<>(true, "获取成功", null, banners));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, "获取轮播图失败: " + e.getMessage()));
        }
    }
}