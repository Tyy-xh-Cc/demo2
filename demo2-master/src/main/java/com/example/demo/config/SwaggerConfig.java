package com.example.demo.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public GroupedOpenApi cakeApi() {
        return GroupedOpenApi.builder()
                .group("蛋糕管理接口")  // Swagger UI 显示的组名
                .pathsToMatch("/api/**")  // 匹配所有以 /api/ 开头的路径
                .build();
    }
}