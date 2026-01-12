package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private static final Logger log = LoggerFactory.getLogger(FileUploadService.class);
    
    @Value("${app.upload.path:./uploads/images/}")
    private String uploadPath;
    
    @Value("${app.static.access-path:/images/}")
    private String staticAccessPath;
    
    /**
     * 上传文件
     * @param file 上传的文件
     * @param subDirectory 子目录（如：banners, products, avatars）
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file, String subDirectory) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只允许上传图片文件");
        }
        
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        
        // 创建目标目录
        String targetDirectory = uploadPath + subDirectory + "/";
        Path targetPath = Paths.get(targetDirectory);
        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }
        
        // 保存文件
        Path targetFile = targetPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        
        log.info("文件上传成功: {} -> {}", originalFilename, targetFile.toString());
        
        // 返回访问URL
        return staticAccessPath + subDirectory + "/" + uniqueFileName;
    }
    
    /**
     * 删除文件
     * @param fileUrl 文件URL（如：/images/banners/filename.jpg）
     */
    public boolean deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }
        
        // 将URL转换为文件路径
        String filePath = convertUrlToPath(fileUrl);
        Path path = Paths.get(filePath);
        
        if (Files.exists(path)) {
            Files.delete(path);
            log.info("文件删除成功: {}", filePath);
            return true;
        }
        
        return false;
    }
    
    /**
     * 检查文件是否存在
     */
    public boolean fileExists(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }
        
        String filePath = convertUrlToPath(fileUrl);
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex).toLowerCase();
    }
    
    /**
     * 将URL转换为文件路径
     */
    private String convertUrlToPath(String fileUrl) {
        // 移除URL开头的路径前缀
        String relativePath = fileUrl.replaceFirst(staticAccessPath, "");
        return uploadPath + relativePath;
    }
    
    /**
     * 获取上传目录的绝对路径
     */
    public String getUploadDir(String subDirectory) {
        return uploadPath + subDirectory + "/";
    }
}