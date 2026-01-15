package com.example.demo.Controller;

import com.example.demo.Service.AddressService;

import com.example.demo.Service.UserService;
import com.example.demo.entity.Dto.RechargeResponse;
import com.example.demo.entity.cakeTableDto.address.AddressDto;
import com.example.demo.entity.cakeTableDto.address.UpdateAddressRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PutMapping("/addresses/{addressId}/default")
    public ResponseEntity<RechargeResponse> setDefaultAddress(
            @PathVariable Integer addressId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 设置默认地址
            boolean success = addressService.setDefaultAddressByToken(addressId, token, userService);

            if (success) {
                RechargeResponse response = new RechargeResponse();
                response.setSuccess(true);
                response.setMessage("设置默认地址成功");
                return ResponseEntity.ok(response);
            } else {
                RechargeResponse response = new RechargeResponse();
                response.setSuccess(false);
                response.setMessage("设置默认地址失败，地址不存在或不属于当前用户");
                return ResponseEntity.status(404).body(response);
            }

        } catch (Exception e) {
            RechargeResponse response = new RechargeResponse();
            response.setSuccess(false);
            response.setMessage("设置默认地址失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/addresses/default")
    public ResponseEntity<AddressDto> getDefaultAddress(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 检查Authorization头是否存在
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(null);
            }

            // 提取token
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            // 获取默认地址
            Optional<AddressDto> defaultAddress = addressService.getDefaultAddressByToken(token, userService);

            // 没有找到默认地址
            return defaultAddress.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
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
            System.out.println(e.getMessage());
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
    public ResponseEntity<AddressDto> updateAddress(@RequestHeader(value = "Authorization", required = false) String authHeader,@PathVariable Integer addressId, @RequestBody UpdateAddressRequest request) {
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
            AddressDto updatedAddress = addressService.updateAddress(userId,addressId, request);
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