package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射物理路径到URL路径
        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                    "classpath:/static/images/",  // 项目内的静态资源
                    "classpath:/static/uploads/", // 上传文件目录
                    "file:./uploads/",            // 项目根目录下的uploads文件夹
                    "file:/var/www/images/"       // 系统绝对路径
                )
                .setCachePeriod(3600); // 设置缓存时间（秒）
        
        // 可以添加更多资源映射
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
}