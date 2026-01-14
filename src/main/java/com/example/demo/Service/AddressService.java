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
            address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

            // 可选字段
            if (request.getLatitude() != null) {
                address.setLatitude(request.getLatitude());
            }
            if (request.getLongitude() != null) {
                address.setLongitude(request.getLongitude());
            }

            addressRepository.save(address);
        } catch (Exception e) {
            throw new RuntimeException("创建地址失败: " + e.getMessage());
        }
    }
    @Transactional
    public AddressDto updateAddress(Integer addressId, UpdateAddressRequest request) {
        try {
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("地址不存在"));
            System.out.println(request.toString());
            System.out.println(address.toString());
            // 更新地址信息
            address.setName(request.getName());
            address.setReceiverName(request.getReceiverName());
            address.setArea(request.getArea());
            address.setPhone(request.getPhone());
            address.setAddress(request.getAddress());
            address.setIsDefault(request.getIsDefault());

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