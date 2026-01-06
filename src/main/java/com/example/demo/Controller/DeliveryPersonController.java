package com.example.demo.Controller;

import com.example.demo.Service.DeliveryPersonService;
import com.example.demo.entity.Dto.LoginResponse;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonQueryDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DeliveryPersonController {
    
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
        this.deliveryPersonService = deliveryPersonService;
    }


    @GetMapping("/deliveryPersons")
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
    @DeleteMapping("/deliveryPersons/{id}")
    public LoginResponse<?> deleteDeliveryPerson(@PathVariable Integer id) {
        boolean deleted = deliveryPersonService.deleteDeliveryPerson(id);
        if (deleted) {
            return new LoginResponse<>(true, "配送员删除成功");
        } else {
            return new LoginResponse<>(false, "配送员不存在");
        }
    }
    @PutMapping("/deliveryPersons/{id}")
    public LoginResponse<?> updateDeliveryPerson(
            @PathVariable Integer id,
            @Valid @RequestBody DeliveryPersonDto requestDto) {
            DeliveryPersonResponseDto responseDto = deliveryPersonService.updateDeliveryPerson(id, requestDto);

            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
    }
    @PostMapping("/deliveryPersons")
    public LoginResponse<?> createDeliveryPerson(
            @Valid @RequestBody DeliveryPersonDto requestDto) {
        try {
            DeliveryPersonResponseDto responseDto = deliveryPersonService.createDeliveryPerson(requestDto);
            if (responseDto.isSuccess()) {
                return new LoginResponse<>(true, responseDto.getMessage());
            } else {
                return new LoginResponse<>(false, responseDto.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return new LoginResponse<>(false, e.getMessage());
        } catch (Exception e) {
            return new LoginResponse<>(false, "创建配送员失败");
        }
    }
}