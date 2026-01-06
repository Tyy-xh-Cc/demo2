package com.example.demo.Service;

import com.example.demo.Repository.DeliveryPersonRepository;
import com.example.demo.entity.Dto.PageRequest;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.DeliveryPerson;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonQueryDto;
import com.example.demo.entity.cakeTableDto.deliveryPersons.DeliveryPersonResponseDto;
import com.example.demo.utility.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    @Transactional
    public DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonDto requestDto) {
        try {
            // 验证必填字段
            if (requestDto.getName() == null || requestDto.getName().trim().isEmpty()) {
                return new DeliveryPersonResponseDto(null, null, null, null, null, null, null,
                        null, null, null, false, "配送员姓名不能为空");
            }
            if (requestDto.getPhone() == null || requestDto.getPhone().trim().isEmpty()) {
                return new DeliveryPersonResponseDto(null, null, null, null, null, null, null,
                        null, null, null, false, "手机号不能为空");
            }

            // 检查手机号是否已存在
            Optional<DeliveryPerson> existingByPhone = deliveryPersonRepository.findByName(requestDto.getPhone());
            if (existingByPhone.isPresent()) {
                return new DeliveryPersonResponseDto(null, null, null, null, null, null, null,
                        null, null, null, false, "手机号已被其他配送员使用");
            }

            // 创建新的配送员实体
            DeliveryPerson deliveryPerson = new DeliveryPerson();
            deliveryPerson.setName(requestDto.getName().trim());
            deliveryPerson.setPhone(requestDto.getPhone().trim());

            // 设置可选字段
            if (requestDto.getIdCard() != null) {
                deliveryPerson.setIdCard(requestDto.getIdCard().trim());
            }
            if (requestDto.getVehicleType() != null) {
                deliveryPerson.setVehicleType(requestDto.getVehicleType().trim());
            }
            if (requestDto.getLicensePlate() != null) {
                deliveryPerson.setLicensePlate(requestDto.getLicensePlate().trim());
            }

            // 设置默认值
            deliveryPerson.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "active");
            deliveryPerson.setRating(requestDto.getRating() != null ? requestDto.getRating() : java.math.BigDecimal.ZERO);
            deliveryPerson.setCompletedOrders(requestDto.getCompletedOrders() != null ? requestDto.getCompletedOrders() : 0);
            deliveryPerson.setCreatedAt(java.time.Instant.now());

            // 保存配送员
            DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

            // 返回创建成功的信息
            return new DeliveryPersonResponseDto(
                    savedDeliveryPerson.getId(),
                    savedDeliveryPerson.getName(),
                    savedDeliveryPerson.getPhone(),
                    savedDeliveryPerson.getIdCard(),
                    savedDeliveryPerson.getVehicleType(),
                    savedDeliveryPerson.getLicensePlate(),
                    savedDeliveryPerson.getStatus(),
                    savedDeliveryPerson.getRating(),
                    savedDeliveryPerson.getCompletedOrders(),
                    savedDeliveryPerson.getCreatedAt(),
                    true,
                    "配送员创建成功"
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("创建配送员失败: " + e.getMessage());
        }
    }
    @Transactional
    public DeliveryPersonResponseDto updateDeliveryPerson(Integer id, DeliveryPersonDto requestDto) {
        try {
            // 检查配送员是否存在
            Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findById(id);
            if (deliveryPersonOptional.isEmpty()) {
                return new DeliveryPersonResponseDto(null, null, null, null, null, null, null,
                        null, null, null, false, "配送员不存在");
            }

            DeliveryPerson deliveryPerson = deliveryPersonOptional.get();

            // 检查手机号是否重复（排除当前配送员）
            if (requestDto.getPhone() != null && !requestDto.getPhone().trim().isEmpty()) {
                Optional<DeliveryPerson> existingByPhone = deliveryPersonRepository.findByName(requestDto.getPhone());
                if (existingByPhone.isPresent() && !existingByPhone.get().getId().equals(id)) {
                    throw new IllegalArgumentException("手机号已被其他配送员使用");
                }
            }

            // 更新字段（只更新非空字段）
            if (requestDto.getName() != null) {
                deliveryPerson.setName(requestDto.getName());
            }
            if (requestDto.getPhone() != null) {
                deliveryPerson.setPhone(requestDto.getPhone());
            }
            if (requestDto.getIdCard() != null) {
                deliveryPerson.setIdCard(requestDto.getIdCard());
            }
            if (requestDto.getVehicleType() != null) {
                deliveryPerson.setVehicleType(requestDto.getVehicleType());
            }
            if (requestDto.getLicensePlate() != null) {
                deliveryPerson.setLicensePlate(requestDto.getLicensePlate());
            }
            if (requestDto.getStatus() != null) {
                deliveryPerson.setStatus(requestDto.getStatus());
            }
            if (requestDto.getRating() != null) {
                deliveryPerson.setRating(requestDto.getRating());
            }
            if (requestDto.getCompletedOrders() != null) {
                deliveryPerson.setCompletedOrders(requestDto.getCompletedOrders());
            }

            // 保存更新
            DeliveryPerson updatedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

            // 返回更新后的信息
            return new DeliveryPersonResponseDto(
                    updatedDeliveryPerson.getId(),
                    updatedDeliveryPerson.getName(),
                    updatedDeliveryPerson.getPhone(),
                    updatedDeliveryPerson.getIdCard(),
                    updatedDeliveryPerson.getVehicleType(),
                    updatedDeliveryPerson.getLicensePlate(),
                    updatedDeliveryPerson.getStatus(),
                    updatedDeliveryPerson.getRating(),
                    updatedDeliveryPerson.getCompletedOrders(),
                    updatedDeliveryPerson.getCreatedAt(),
                    true,
                    "配送员信息更新成功"
            );

        } catch (IllegalArgumentException e) {
            throw e; // 重新抛出验证异常
        } catch (Exception e) {
            throw new IllegalArgumentException("更新配送员信息失败: " + e.getMessage());
        }
    }
    @Transactional
    public boolean deleteDeliveryPerson(Integer id) {
        try {
            // 检查配送员是否存在
            Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findById(id);
            if (deliveryPersonOptional.isEmpty()) {
                return false; // 配送员不存在
            }

            DeliveryPerson deliveryPerson = deliveryPersonOptional.get();

            // 检查是否有未完成的配送任务
            if (hasActiveDeliveries(deliveryPerson)) {
                throw new IllegalArgumentException("该配送员有未完成的配送任务，无法删除");
            }

            // 执行删除
            deliveryPersonRepository.delete(deliveryPerson);
            return true;

        } catch (Exception e) {
            throw new IllegalArgumentException("删除配送员失败: " + e.getMessage());
        }
    }

    /**
     * 检查配送员是否有未完成的配送任务
     */
    private boolean hasActiveDeliveries(DeliveryPerson deliveryPerson) {
        // 这里需要检查配送员是否有状态为assigned、picking_up、delivering的配送任务
        // 由于Delivery实体关联关系，这里简化处理
        return deliveryPerson.getDeliveries() != null &&
                !deliveryPerson.getDeliveries().isEmpty() &&
                deliveryPerson.getDeliveries().stream()
                        .anyMatch(delivery ->
                                "assigned".equals(delivery.getStatus()) ||
                                        "picking_up".equals(delivery.getStatus()) ||
                                        "delivering".equals(delivery.getStatus())
                        );
    }
}