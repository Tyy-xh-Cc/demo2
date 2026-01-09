package com.example.demo.Controller;

import com.example.demo.Service.RestaurantService;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;

import com.example.demo.entity.cakeTableDto.restaurant.RestaurantDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantQueryDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantRequestDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RestaurantController {
    
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }


    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantResponseDto> addRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        try {
            RestaurantResponseDto responseDto = restaurantService.addRestaurant(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @PathVariable Integer id,
            @Valid @RequestBody RestaurantRequestDto requestDto) {
        try {
            RestaurantResponseDto responseDto = restaurantService.updateRestaurant(id, requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/paged/restaurants")
    public PageResponse<RestaurantDto> getPagedRestaurants(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {

        PageRequest pageRequest = new PageRequest(page, size);
        RestaurantQueryDto queryDto = new RestaurantQueryDto(name, phone, address, status, minRating, maxRating);

        return restaurantService.getRestaurantsByConditions(pageRequest, queryDto);
    }
}