package com.example.demo.Service;

import com.example.demo.Repository.AddressRepository;
import com.example.demo.entity.cakeTable.Address;

import com.example.demo.entity.cakeTableDto.address.AddressDto;
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
                address.getPhone(),
                address.getAddress(),
                address.getLatitude(),
                address.getLongitude(),
                address.getIsDefault(),
                address.getCreatedAt()
        );
    }
}