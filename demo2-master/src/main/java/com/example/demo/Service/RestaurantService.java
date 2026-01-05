package com.example.demo.Service;

import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.utility.BaseService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService extends BaseService {
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

}
