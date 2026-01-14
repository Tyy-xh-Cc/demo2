package com.example.demo.Controller;

import com.example.demo.Service.AddressService;

import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.RechargeResponse;
import com.example.demo.entity.cakeTableDto.address.AddressDto;
import com.example.demo.entity.cakeTableDto.address.UpdateAddressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;
    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }
    @PostMapping("/addresses")
    public ResponseEntity<RechargeResponse> createAddress(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                        @RequestBody UpdateAddressRequest request) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀
            // 根据token获取用户ID
            System.out.println(request.getArea());
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }
            addressService.createAddress(userId, request);
            RechargeResponse response=new RechargeResponse();
            response.setSuccess(true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId) {
        try {
            boolean deleted = addressService.deleteAddress(addressId);
            if (deleted) {
                return ResponseEntity.ok("地址删除成功");
            } else {
                return ResponseEntity.status(404).body("地址不存在");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("删除地址失败: " + e.getMessage());
        }
    }
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Integer addressId, @RequestBody UpdateAddressRequest request) {
        try {
            AddressDto updatedAddress = addressService.updateAddress(addressId, request);
            return ResponseEntity.ok(updatedAddress);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("地址不存在")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/addresses/my")
    public ResponseEntity<List<AddressDto>> getAddressesByToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return ResponseEntity.status(401).body(null);
            }

            // 根据用户ID获取地址列表
            List<AddressDto> addresses = addressService.getAddressesByUserId(userId);
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