package com.example.demo.utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {
    
    /**
     * 压缩图片
     */
    public static byte[] compressImage(byte[] imageBytes, String formatName, int maxWidth, int maxHeight) 
            throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(bis);
        bis.close();
        
        if (originalImage == null) {
            throw new IOException("无法读取图片数据");
        }
        
        // 计算缩放比例
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        // 如果宽度超过最大宽度
        if (originalWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) ((double) originalHeight / originalWidth * maxWidth);
        }
        
        // 如果高度超过最大高度
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) ((double) originalWidth / originalHeight * maxHeight);
        }
        
        // 创建缩放后的图片
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        // 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, formatName, baos);
        
        return baos.toByteArray();
    }
    
    /**
     * 生成缩略图
     */
    public static byte[] createThumbnail(byte[] imageBytes, String formatName, int thumbnailSize) 
            throws IOException {
        return compressImage(imageBytes, formatName, thumbnailSize, thumbnailSize);
    }
    
    /**
     * 获取图片尺寸
     */
    public static Dimension getImageDimension(byte[] imageBytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);
        bis.close();
        
        if (image == null) {
            return null;
        }
        
        return new Dimension(image.getWidth(), image.getHeight());
    }
}