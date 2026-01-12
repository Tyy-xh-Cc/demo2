package com.example.demo.Service;

import com.example.demo.Repository.BannerRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.Banner;
import com.example.demo.entity.cakeTableDto.banners.BannerDto;
import com.example.demo.utility.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BannerService extends BaseService {
    
    private final BannerRepository bannerRepository;
    private final FileUploadService fileUploadService;
    public BannerService(BannerRepository bannerRepository, FileUploadService fileUploadService) {
        this.bannerRepository = bannerRepository;
        this.fileUploadService = fileUploadService;
    }
    
    /**
     * 获取有效的轮播图列表（前端首页用）
     */
    public List<BannerDto> getActiveBanners() {
        Instant now = Instant.now();
        List<Banner> banners = bannerRepository.findActiveBanners(now);
        
        return banners.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页查询轮播图（后台管理用）
     */
    public PageResponse<BannerDto> getBannersByConditions(PageRequest pageRequest, String title, String status, String linkType) {
        Pageable pageable = buildPageable(pageRequest);
        
        Page<Banner> bannerPage;
        
        if ((title == null || title.trim().isEmpty()) && 
            (status == null || status.trim().isEmpty()) && 
            (linkType == null || linkType.trim().isEmpty())) {
            // 无条件查询
            bannerPage = bannerRepository.findAll(pageable);
        } else {
            // 条件查询
            bannerPage = bannerRepository.findByConditions(
                    title, 
                    status, 
                    linkType, 
                    pageable);
        }
        
        List<BannerDto> bannerDtos = bannerPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return convertToPageResponse(bannerPage, bannerDtos);
    }
    
    /**
     * 根据状态获取轮播图
     */
    public List<BannerDto> getBannersByStatus(String status) {
        List<Banner> banners = bannerRepository.findByStatus(status);
        
        return banners.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据ID获取轮播图
     */
    public BannerDto getBannerById(Integer id) {
        return bannerRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }
    
    /**
     * 创建轮播图
     */
    @Transactional
    public BannerDto createBanner(BannerDto bannerDto) {
        Banner banner = new Banner();
        banner.setTitle(bannerDto.getTitle());
        banner.setImageUrl(bannerDto.getImageUrl());
        banner.setLinkUrl(bannerDto.getLinkUrl());
        banner.setLinkType(bannerDto.getLinkType());
        banner.setLinkId(bannerDto.getLinkId());
        banner.setSortOrder(bannerDto.getSortOrder());
        banner.setStatus(bannerDto.getStatus() != null ? bannerDto.getStatus() : "active");
        banner.setStartTime(bannerDto.getStartTime());
        banner.setEndTime(bannerDto.getEndTime());
        banner.setCreatedAt(Instant.now());
        banner.setUpdatedAt(Instant.now());
        
        Banner savedBanner = bannerRepository.save(banner);
        
        return convertToDto(savedBanner);
    }
    
    /**
     * 更新轮播图
     */
    @Transactional
    public BannerDto updateBanner(Integer id, BannerDto bannerDto) {
        return bannerRepository.findById(id)
                .map(existingBanner -> {
                    if (bannerDto.getTitle() != null) {
                        existingBanner.setTitle(bannerDto.getTitle());
                    }
                    if (bannerDto.getImageUrl() != null) {
                        existingBanner.setImageUrl(bannerDto.getImageUrl());
                    }
                    if (bannerDto.getLinkUrl() != null) {
                        existingBanner.setLinkUrl(bannerDto.getLinkUrl());
                    }
                    if (bannerDto.getLinkType() != null) {
                        existingBanner.setLinkType(bannerDto.getLinkType());
                    }
                    if (bannerDto.getLinkId() != null) {
                        existingBanner.setLinkId(bannerDto.getLinkId());
                    }
                    if (bannerDto.getSortOrder() != null) {
                        existingBanner.setSortOrder(bannerDto.getSortOrder());
                    }
                    if (bannerDto.getStatus() != null) {
                        existingBanner.setStatus(bannerDto.getStatus());
                    }
                    if (bannerDto.getStartTime() != null) {
                        existingBanner.setStartTime(bannerDto.getStartTime());
                    }
                    if (bannerDto.getEndTime() != null) {
                        existingBanner.setEndTime(bannerDto.getEndTime());
                    }
                    existingBanner.setUpdatedAt(Instant.now());
                    
                    Banner updatedBanner = bannerRepository.save(existingBanner);
                    return convertToDto(updatedBanner);
                })
                .orElse(null);
    }
    
    /**
     * 删除轮播图
     */
    @Transactional
    public boolean deleteBanner(Integer id) {
        if (bannerRepository.existsById(id)) {
            bannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * 更新轮播图状态
     */
    @Transactional
    public BannerDto updateBannerStatus(Integer id, String status) {
        return bannerRepository.findById(id)
                .map(banner -> {
                    banner.setStatus(status);
                    banner.setUpdatedAt(Instant.now());
                    Banner updatedBanner = bannerRepository.save(banner);
                    return convertToDto(updatedBanner);
                })
                .orElse(null);
    }
    /**
     * 转换实体为DTO
     */
    private BannerDto convertToDto(Banner banner) {
        return new BannerDto(
                banner.getId(),
                banner.getTitle(),
                banner.getImageUrl(),
                banner.getLinkUrl(),
                banner.getLinkType(),
                banner.getLinkId(),
                banner.getSortOrder(),
                banner.getStatus(),
                banner.getStartTime(),
                banner.getEndTime(),
                banner.getCreatedAt()
        );
    }
}