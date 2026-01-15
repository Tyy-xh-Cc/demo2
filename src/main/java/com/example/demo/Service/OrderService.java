package com.example.demo.Service;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.OrderItemRepository;
import com.example.demo.Repository.RestaurantRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.entity.Dto.PageResponse;
import com.example.demo.entity.cakeTable.Order;
import com.example.demo.entity.cakeTable.OrderItem;
import com.example.demo.entity.cakeTable.Restaurant;
import com.example.demo.entity.cakeTable.Product;
import com.example.demo.entity.cakeTable.User;
import com.example.demo.entity.cakeTableDto.order.CreateOrderRequest;
import com.example.demo.entity.cakeTableDto.order.OrderDto;
import com.example.demo.entity.cakeTableDto.order.OrderItemRequest;
import com.example.demo.entity.cakeTableDto.order.OrderQueryRequest;
import com.example.demo.utility.BaseService;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService extends BaseService {

    private static final Log log = LogFactory.getLog(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, 
                       OrderItemRepository orderItemRepository,
                       RestaurantRepository restaurantRepository,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }
    public PageResponse<OrderDto> getOrdersByUserIdWithPagination(Integer userId, OrderQueryRequest queryRequest) {
        try {
            // 创建分页和排序对象
            Sort sort = Sort.by(queryRequest.getSortOrder().equalsIgnoreCase("asc") ?
                            Sort.Direction.ASC : Sort.Direction.DESC,
                    queryRequest.getSortBy());

            Pageable pageable = PageRequest.of(queryRequest.getPage() - 1, queryRequest.getSize(), sort);

            // 创建查询条件
            Specification<Order> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // 用户ID条件
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

                // 订单状态条件（如果有）
                if (queryRequest.getStatus() != null && !queryRequest.getStatus().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), queryRequest.getStatus()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            // 执行分页查询
            Page<Order> orderPage = orderRepository.findAll(specification, pageable);

            // 转换为DTO列表
            List<OrderDto> orderDtos = orderPage.getContent().stream()
                    .map(this::convertToOrderDto)
                    .collect(Collectors.toList());

            // 构建分页响应
            PageResponse<OrderDto> response = new PageResponse<>();
            response.setContent(orderDtos);
            response.setPage(queryRequest.getPage());
            response.setSize(queryRequest.getSize());
            response.setTotalElements(orderPage.getTotalElements());
            response.setTotalPages(orderPage.getTotalPages());
            response.setLast(orderPage.isLast());
            response.setFirst(orderPage.isFirst());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询所有订单（管理员接口）
     */
    public PageResponse<OrderDto> getAllOrdersWithPagination(OrderQueryRequest queryRequest) {
        try {
            // 创建分页和排序对象
            Sort sort = Sort.by(queryRequest.getSortOrder().equalsIgnoreCase("asc") ?
                            Sort.Direction.ASC : Sort.Direction.DESC,
                    queryRequest.getSortBy());

            Pageable pageable = PageRequest.of(queryRequest.getPage() - 1, queryRequest.getSize(), sort);

            // 创建查询条件
            Specification<Order> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // 订单状态条件（如果有）
                if (queryRequest.getStatus() != null && !queryRequest.getStatus().isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), queryRequest.getStatus()));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            // 执行分页查询
            Page<Order> orderPage = orderRepository.findAll(specification, pageable);

            // 转换为DTO列表
            List<OrderDto> orderDtos = orderPage.getContent().stream()
                    .map(this::convertToOrderDto)
                    .collect(Collectors.toList());

            // 构建分页响应
            PageResponse<OrderDto> response = new PageResponse<>();
            response.setContent(orderDtos);
            response.setPage(queryRequest.getPage());
            response.setSize(queryRequest.getSize());
            response.setTotalElements(orderPage.getTotalElements());
            response.setTotalPages(orderPage.getTotalPages());
            response.setLast(orderPage.isLast());
            response.setFirst(orderPage.isFirst());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("获取订单列表失败: " + e.getMessage());
        }
    }
    /**
     * 创建订单
     */
    @Transactional
    public OrderDto createOrder(Integer userId, CreateOrderRequest request) {
        try {
            // 生成订单ID
            String orderId = generateOrderId();
            System.out.println(request.toString()+request.getItems().get(0).toString());
            // 验证餐厅是否存在
            Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("餐厅不存在"));

            // 创建用户实体
            User user = new User();
            user.setId(userId);

            // 创建订单实体
            Order order = new Order();
            order.setOrderId(orderId);
            order.setUser(user);
            order.setRestaurant(restaurant);
            order.setTotalAmount(request.getTotalAmount());
            order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : BigDecimal.ZERO);
            order.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
            order.setFinalAmount(request.getFinalAmount());
            order.setDeliveryAddress(request.getDeliveryAddress());
            order.setDeliveryPhone(request.getDeliveryPhone());
            order.setDeliveryName(request.getDeliveryName());
            order.setNote(request.getNote());
            order.setStatus("pending"); // 待处理
            order.setPaymentMethod(request.getPaymentMethod());
            order.setPaymentStatus("pending"); // 待支付
            order.setCreatedAt(Instant.now());

            // 保存订单
            Order savedOrder = orderRepository.save(order);

            // 创建订单项
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                for (OrderItemRequest itemRequest : request.getItems()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);

                    // 设置产品
                    Product product = new Product();
                    product.setId(itemRequest.getProductId());
                    product.setName(itemRequest.getProductName());
                    product.setPrice(itemRequest.getPrice());
                    product.setOriginalPrice(itemRequest.getTotalPrice());
                    orderItem.setProduct(product);

                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPrice(itemRequest.getPrice());
                    orderItem.setProductName(itemRequest.getProductName());



                    orderItemRepository.save(orderItem);
                }
            }

            return convertToOrderDto(savedOrder);

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 生成订单ID
     */
    private String generateOrderId() {
        return "O" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 根据订单ID获取订单
     */
    public OrderDto getOrderById(String orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("订单不存在"));
            return convertToOrderDto(order);
        } catch (Exception e) {
            throw new RuntimeException("获取订单失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取订单列表
     */
    public List<OrderDto> getOrdersByUserId(Integer userId) {
        try {
            User user = new User();
            user.setId(userId);
            List<Order> orders = orderRepository.findByUser(user);
            return orders.stream()
                    .map(this::convertToOrderDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 将Order实体转换为OrderDto
     */
    private OrderDto convertToOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getId());
        dto.setRestaurantId(order.getRestaurant().getId());
        dto.setRestaurantName(order.getRestaurant().getName());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setFinalAmount(order.getFinalAmount());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setDeliveryPhone(order.getDeliveryPhone());
        dto.setDeliveryName(order.getDeliveryName());
        dto.setNote(order.getNote());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaidAt(order.getPaidAt());
        dto.setCompletedAt(order.getCompletedAt());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
