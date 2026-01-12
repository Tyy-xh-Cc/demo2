package com.example.demo.Controller;

import com.example.demo.Service.AddressService;

import com.example.demo.entity.cakeTableDto.address.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * 获取地址列表
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddressList() {
        try {
            List<AddressDto> addresses = addressService.getAllAddresses();
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 根据用户ID获取地址列表
     */
    @GetMapping("/users/{userId}/addresses")
    public ResponseEntity<List<AddressDto>> getAddressesByUserId(@PathVariable Integer userId) {
        try {
            List<AddressDto> addresses = addressService.getAddressesByUserId(userId);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}