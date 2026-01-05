package com.example.demo.Controller;

import com.example.demo.Service.OrderService;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTableDto.Order.OrderDto;
import com.example.demo.entity.cakeTableDto.Order.OrderQueryDot;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }
    @GetMapping("/paged/orders")
    public PageResponse<OrderDto> getPagedUsersByConditions(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String status) {

        PageRequest pageRequest = new PageRequest(page, size);
        OrderQueryDot queryDto = new OrderQueryDot(orderId,userName,status);

        return service.getUsersByConditions(pageRequest, queryDto);
    }
}
