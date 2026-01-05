package com.example.demo.Service;

import com.example.demo.Repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private  final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

}
