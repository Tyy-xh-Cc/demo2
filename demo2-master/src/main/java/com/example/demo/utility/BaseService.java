package com.example.demo.utility;

import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.User;
import com.example.demo.entity.cakeTableDto.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BaseService {

    // 将User转换为UserDto的私有方法
    protected UserDto convertToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getStatus(),
                user.getIdentity()
        );
    }

    // 构建Pageable对象
    protected Pageable buildPageable(PageRequest pageRequest) {
        return org.springframework.data.domain.PageRequest.of(
                pageRequest.getPage() - 1, // 前端从1开始，JPA从0开始
                pageRequest.getSize()
        );
    }

    // 转换为PageResponse
    protected <T> PageResponse<T> convertToPageResponse(Page<?> page, List<T> content) {
        return new PageResponse<>(
                content,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements()
        );
    }
}
