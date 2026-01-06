package com.example.demo.Controller;

import com.example.demo.Service.DeliveryPersonService;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonQueryDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DeliveryPersonController {
    
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
        this.deliveryPersonService = deliveryPersonService;
    }

    /**
     * 配送员分页查询接口
     * GET /api/delivery-persons?page=0&size=10&name=张&status=online&minRating=4.0
     */
    @GetMapping("/delivery-persons")
    public PageResponse<DeliveryPersonDto> getPagedDeliveryPersons(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {

        PageRequest pageRequest = new PageRequest(page, size);
        DeliveryPersonQueryDto queryDto = new DeliveryPersonQueryDto(name, phone, status, vehicleType, minRating, maxRating);

        return deliveryPersonService.getDeliveryPersonsByConditions(pageRequest, queryDto);
    }
}