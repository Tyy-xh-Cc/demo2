package com.example.demo.Service;

import com.example.demo.Repository.DeliveryPersonRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.DeliveryPerson;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonQueryDto;
import com.example.demo.entity.cakeTableDto.restaurant.RestaurantDto;
import com.example.demo.utility.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryPersonService extends BaseService {
    
    private final DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    /**
     * 分页查询配送员
     */
    public PageResponse<DeliveryPersonDto> getDeliveryPersonsByConditions(PageRequest pageRequest, DeliveryPersonQueryDto queryDto) {
        Pageable pageable = buildPageable(pageRequest);
        
        Page<DeliveryPerson> deliveryPersonPage;
        
        if (queryDto.isEmpty()) {
            // 无条件查询
            deliveryPersonPage = deliveryPersonRepository.findAll(pageable);
        } else {
            // 条件查询
            deliveryPersonPage = deliveryPersonRepository.findByConditions(
                queryDto.getName(),
                queryDto.getPhone(),
                queryDto.getStatus(),
                queryDto.getVehicleType(),
                queryDto.getMinRating(),
                queryDto.getMaxRating(),
                pageable
            );
        }

        List<DeliveryPersonDto> deliveryPersonDtos = deliveryPersonPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return convertToPageResponse(deliveryPersonPage, deliveryPersonDtos);
    }

    /**
     * 实体转换为DTO
     */
    private DeliveryPersonDto convertToDto(DeliveryPerson deliveryPerson) {
        return new DeliveryPersonDto(
            deliveryPerson.getId(),
            deliveryPerson.getName(),
            deliveryPerson.getPhone(),
            deliveryPerson.getIdCard(),
            deliveryPerson.getVehicleType(),
            deliveryPerson.getLicensePlate(),
            deliveryPerson.getStatus(),
            deliveryPerson.getRating(),
            deliveryPerson.getCompletedOrders(),
            deliveryPerson.getCreatedAt()
        );
    }
}