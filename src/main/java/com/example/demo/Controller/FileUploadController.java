package com.example.demo.Controller;

import com.example.demo.Service.FileUploadService;
import com.example.demo.entity.Dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
@CrossOrigin(origins = "*")
public class FileUploadController {
    
    private final FileUploadService fileUploadService;
    
    @Value("${app.static.access-path:/images/}")
    private String staticAccessPath;
    
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    /**
     * 上传图片
     * POST /api/common/upload/image
     */
    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "banners") String type) {
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "上传的文件为空"));
            }
            
            // 检查文件大小（限制为5MB）
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "文件大小不能超过5MB"));
            }
            
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "只允许上传图片文件"));
            }
            
            // 验证文件扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "文件名不能为空"));
            }
            
            String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
            boolean validExtension = false;
            for (String ext : allowedExtensions) {
                if (originalFilename.toLowerCase().endsWith(ext)) {
                    validExtension = true;
                    break;
                }
            }
            
            if (!validExtension) {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "不支持的文件格式，仅支持jpg, jpeg, png, gif, webp"));
            }
            
            // 上传文件
            String fileUrl = fileUploadService.uploadFile(file, type);
            
            // 返回成功响应
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "文件上传成功");
            result.put("url", fileUrl);
            result.put("originalName", originalFilename);
            result.put("size", file.getSize());
            result.put("contentType", contentType);
            
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse<>(false, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse<>(false, "文件上传失败: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse<>(false, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 上传轮播图图片（特定接口）
     * POST /api/common/upload/banner
     */
    @PostMapping("/upload/banner")
    public ResponseEntity<?> uploadBannerImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "banners");
    }
    
    /**
     * 上传商品图片
     * POST /api/common/upload/product
     */
    @PostMapping("/upload/product")
    public ResponseEntity<?> uploadProductImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "products");
    }
    
    /**
     * 上传用户头像
     * POST /api/common/upload/avatar
     */
    @PostMapping("/upload/avatar")
    public ResponseEntity<?> uploadAvatarImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "avatars");
    }
    
    /**
     * 删除图片
     * DELETE /api/common/upload/image
     */
    @DeleteMapping("/upload/image")
    public ResponseEntity<?> deleteImage(@RequestParam("url") String fileUrl) {
        try {
            boolean deleted = fileUploadService.deleteFile(fileUrl);
            
            if (deleted) {
                return ResponseEntity.ok(new LoginResponse<>(true, "文件删除成功"));
            } else {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse<>(false, "文件不存在或删除失败"));
            }
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse<>(false, "文件删除失败: " + e.getMessage()));
        }
    }
    
    /**
     * 检查图片是否存在
     * GET /api/common/upload/check
     */
    @GetMapping("/upload/check")
    public ResponseEntity<?> checkImage(@RequestParam("url") String fileUrl) {
        boolean exists = fileUploadService.fileExists(fileUrl);
        
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("url", fileUrl);
        
        return ResponseEntity.ok(result);
    }
}