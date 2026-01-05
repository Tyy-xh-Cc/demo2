package com.example.demo.Service;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.Order;
import com.example.demo.entity.cakeTableDto.Order.OrderDto;
import com.example.demo.entity.cakeTableDto.Order.OrderQueryDot;
import com.example.demo.utility.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService extends BaseService {
    private  final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }
    public PageResponse<OrderDto> getUsersByConditions(PageRequest pageRequest, OrderQueryDot queryDto) {
        Pageable pageable = buildPageable(pageRequest);
        Page<Order> userPage;

        // 根据查询条件选择不同的查询方法
        if (queryDto.isEmpty()) {
            // 如果没有查询条件，查询所有用户
            userPage = repository.findAll(pageable);
        } else {
            // 使用复杂条件查询
            userPage = repository.findByConditions(
                    queryDto.getOrderId(),
                    queryDto.getDeliveryName(),
                    queryDto.getStatus(),
                    pageable
            );
        }

        List<OrderDto> orderDto = userPage.getContent().stream()
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());

        return convertToPageResponse(userPage, orderDto);
    }
}
