package com.example.demo.Service;

import com.example.demo.Repository.AddressRepository;
import com.example.demo.entity.cakeTable.Address;

import com.example.demo.entity.cakeTable.User;
import com.example.demo.entity.cakeTableDto.address.AddressDto;
import com.example.demo.entity.cakeTableDto.address.UpdateAddressRequest;
import com.example.demo.utility.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AddressService extends BaseService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    @Transactional
    public boolean deleteAddress(Integer addressId) {
        try {
            if (addressRepository.existsById(addressId)) {
                addressRepository.deleteById(addressId);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public Optional<AddressDto> getDefaultAddressByToken(String token, UserService userService) {
        try {
            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return Optional.empty();
            }

            return getDefaultAddressByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("获取默认地址失败: " + e.getMessage());
        }
    }
    public Optional<AddressDto> getDefaultAddressByUserId(Integer userId) {
        try {
            Optional<Address> defaultAddress = addressRepository.findDefaultAddressByUserId(userId);
            return defaultAddress.map(this::convertToAddressDto);
        } catch (Exception e) {
            throw new RuntimeException("获取默认地址失败: " + e.getMessage());
        }
    }
    @Transactional
    public void createAddress(Integer userId, UpdateAddressRequest request) {
        try {
            // 创建新的地址实体
            Address address = new Address();

            // 设置用户关联（需要先获取User实体）
            User user = new User();
            user.setId(userId);
            address.setUser(user);

            // 设置地址信息
            address.setName(request.getName());
            address.setReceiverName(request.getReceiverName());
            address.setPhone(request.getPhone());
            address.setAddress(request.getAddress());
            address.setArea(request.getArea());

            // 处理默认地址逻辑
            Boolean isDefault = request.getIsDefault() != null ? request.getIsDefault() : false;
            address.setIsDefault(isDefault);

            // 如果设置为默认地址，取消其他地址的默认状态
            if (isDefault) {
                addressRepository.unsetAllDefaultAddressesByUserId(userId);
            }

            // 可选字段
            if (request.getLatitude() != null) {
                address.setLatitude(request.getLatitude());
            }
            if (request.getLongitude() != null) {
                address.setLongitude(request.getLongitude());
            }

            addressRepository.save(address);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("创建地址失败: " + e.getMessage());
        }
    }

    @Transactional
    public AddressDto updateAddress(Integer userId, Integer addressId, UpdateAddressRequest request) {
        try {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("地址不存在"));

            Boolean oldIsDefault = address.getIsDefault();

            // 处理默认地址逻辑
            if (request.getIsDefault() && (!oldIsDefault || !address.getUser().getId().equals(userId))) {
                // 如果设置为默认地址，且之前不是默认地址，或者用户ID发生变化
                // 先取消该用户所有地址的默认状态
                addressRepository.unsetAllDefaultAddressesByUserId(userId);

                // 然后设置当前地址为默认地址
                address.setIsDefault(true);

                // 更新其他字段
                address.setName(request.getName());
                address.setReceiverName(request.getReceiverName());
                address.setArea(request.getArea());
                address.setPhone(request.getPhone());
                address.setAddress(request.getAddress());

            } else if (!request.getIsDefault() && oldIsDefault) {
                // 如果从默认地址变为非默认地址
                address.setIsDefault(false);

                // 更新其他字段
                address.setName(request.getName());
                address.setReceiverName(request.getReceiverName());
                address.setArea(request.getArea());
                address.setPhone(request.getPhone());
                address.setAddress(request.getAddress());

            } else {
                // 默认状态没有变化，只更新其他字段
                address.setName(request.getName());
                address.setReceiverName(request.getReceiverName());
                address.setArea(request.getArea());
                address.setPhone(request.getPhone());
                address.setAddress(request.getAddress());
                address.setIsDefault(request.getIsDefault());
            }

            // 可选字段更新
            if (request.getLatitude() != null) {
                address.setLatitude(request.getLatitude());
            }
            if (request.getLongitude() != null) {
                address.setLongitude(request.getLongitude());
            }

            Address updatedAddress = addressRepository.save(address);
            return convertToAddressDto(updatedAddress);

        } catch (Exception e) {
            throw new RuntimeException("更新地址失败: " + e.getMessage());
        }
    }

    /**
     * 设置地址为默认地址
     */
    @Transactional
    public boolean setDefaultAddress(Integer addressId, Integer userId) {
        try {
            // 验证地址是否存在且属于该用户
            Optional<Address> addressOpt = addressRepository.findById(addressId);
            if (addressOpt.isEmpty()) {
                return false;
            }

            Address address = addressOpt.get();
            if (!address.getUser().getId().equals(userId)) {
                return false; // 地址不属于当前用户
            }

            // 取消该用户所有地址的默认状态
            addressRepository.unsetAllDefaultAddressesByUserId(userId);

            // 设置指定地址为默认地址
            addressRepository.setAddressAsDefault(addressId);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("设置默认地址失败: " + e.getMessage());
        }
    }

    /**
     * 通过token设置默认地址
     */
    @Transactional
    public boolean setDefaultAddressByToken(Integer addressId, String token, UserService userService) {
        try {
            // 根据token获取用户ID
            Integer userId = userService.getUserByToken(token) != null ?
                    userService.getUserByToken(token).getId() : null;

            if (userId == null) {
                return false;
            }

            return setDefaultAddress(addressId, userId);
        } catch (Exception e) {
            throw new RuntimeException("设置默认地址失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有地址列表
     */
    public List<AddressDto> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(this::convertToAddressDto)
                .collect(Collectors.toList());
    }

    public List<AddressDto> getAddressesByUserId(Integer userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(this::convertToAddressDto)
                .collect(Collectors.toList());
    }

    /**
     * 将Address实体转换为AddressDto
     */
    private AddressDto convertToAddressDto(Address address) {
        return new AddressDto(
                address.getId(),
                address.getUser().getId(), // 获取用户ID
                address.getName(),
                address.getReceiverName(),
                address.getArea(),
                address.getPhone(),
                address.getAddress(),
                address.getLatitude(),
                address.getLongitude(),
                address.getIsDefault(),
                address.getCreatedAt()
        );
    }
}