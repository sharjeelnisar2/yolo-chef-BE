package com.yolo.chef.order;


import com.yolo.chef.address.Address;
import com.yolo.chef.address.AddressRepository;
import com.yolo.chef.order.dto.OrderDetailsResponse;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.order.dto.OrderResponse;
import com.yolo.chef.orderItem.OrderItem;
import com.yolo.chef.orderItem.OrderItemRepository;
import com.yolo.chef.orderStatus.OrderStatusService;
import com.yolo.chef.recipe.RecipeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private RecipeService recipeService;
    @Transactional
    public void saveOrder(OrderRequest.OrderDTO orderDTO) {
        Address address = new Address();
        address.setHouse(orderDTO.getAddress().getHouse());
        address.setStreet(orderDTO.getAddress().getStreet());
        address.setArea(orderDTO.getAddress().getArea());
        address.setZip_code(orderDTO.getAddress().getZip_code());
        address.setCity(orderDTO.getAddress().getCity());
        address.setCountry(orderDTO.getAddress().getCountry());
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        Address savedAddress = addressRepository.save(address);

        Order order = new Order();
        order.setPrice(orderDTO.getTotal_price());
        order.setCode(orderDTO.getOrder_code());
        order.setCustomerContactNumber(orderDTO.getCustomer_contact_number());
        order.setCustomerName(orderDTO.getCustomer_name());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setAddressId(savedAddress.getId());
        order.setOrderStatusId(1);
        order.setChefId(getChefIdFromCode(orderDTO.getOrder_items().get(0).getRecipe_code()));
        Order savedOrder = orderRepository.save(order);

        for (OrderRequest.OrderItemDTO itemDTO : orderDTO.getOrder_items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setRecipeId(getRecipeIdFromCode(itemDTO.getRecipe_code()));
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItemRepository.save(orderItem);
        }
    }


public Map<String, Object> getOrdersByChefId(
        Integer chefId, Integer orderStatusId, Long minPrice, Long maxPrice,
        String search, Pageable pageable) {

    Page<Order> orders = orderRepository.findByChefIdAndOrderStatusIdAndPriceBetweenAndCustomerContactNumberContainingOrCodeContaining(
            chefId, orderStatusId, minPrice != null ? minPrice : Long.MIN_VALUE, maxPrice != null ? maxPrice : Long.MAX_VALUE,
            search != null ? search : "", search != null ? search : "", pageable);

    List<OrderResponse> orderResponses = orders.stream()
            .map(order -> new OrderResponse(
                    order.getId(),
                    order.getPrice(),
                    order.getCustomerName(),
                    order.getCustomerContactNumber(),
                    orderStatusService.getStatusByOrderId(order.getOrderStatusId()), // Assuming getStatusById returns a status description
                    order.getCreatedAt()
            ))
            .collect(Collectors.toList());

    // Format the response
        return Map.of(
                "current_page", orders.getNumber() + 1,
                "page_size", orders.getSize(),
                "total_items", orders.getTotalElements(),
                "total_pages", orders.getTotalPages(),
                "orders", orderResponses
        );
}

    public OrderDetailsResponse getOrderDetails(Integer orderId) {
        List<Object[]> results = orderRepository.findOrderDetailsById(orderId);

        if (results.isEmpty()) {
            throw new RuntimeException("Order not found");
        }

        Object[] firstResult = results.get(0);

        // Initialize the response DTO
        OrderDetailsResponse response = new OrderDetailsResponse();

        // Map the OrderDTO
        OrderDetailsResponse.OrderDTO orderDTO = new OrderDetailsResponse.OrderDTO();
        orderDTO.setTotal_price((Long) firstResult[0]);
        orderDTO.setCustomer_contact_number((String) firstResult[1]);
        orderDTO.setCustomer_name((String) firstResult[2]);
        orderDTO.setCreated_at(((Timestamp) firstResult[3]).toLocalDateTime());
        orderDTO.setCurrency_code("USD");
        orderDTO.setStatus((String) firstResult[4]);

        // Map the AddressDTO
        OrderDetailsResponse.OrderDTO.AddressDTO addressDTO = new OrderDetailsResponse.OrderDTO.AddressDTO();
        addressDTO.setHouse((String) firstResult[5]);
        addressDTO.setStreet((String) firstResult[6]);
        addressDTO.setArea((String) firstResult[7]);
        addressDTO.setZip_code((String) firstResult[8]);
        addressDTO.setCity((String) firstResult[9]);
        addressDTO.setCountry((String) firstResult[10]);
        orderDTO.setAddress(addressDTO);

        // Map the OrderItemDTOs
        List<OrderDetailsResponse.OrderItemDTO> orderItems = new ArrayList<>();
        for (Object[] result : results) {
            OrderDetailsResponse.OrderItemDTO orderItemDTO = new OrderDetailsResponse.OrderItemDTO();
            orderItemDTO.setRecipe_name((String) result[11]);
            orderItemDTO.setQuantity((Long) result[12]);
            orderItemDTO.setPrice((Long) result[13]);
            orderItemDTO.setServing_size((Integer) result[14]);

            orderItems.add(orderItemDTO);
        }

        orderDTO.setOrder_items(orderItems);
        response.setOrder(orderDTO);

        return response;
    }

    // Mock method for getting recipe ID from code
    private Integer getRecipeIdFromCode(String recipeCode) {
       int id= recipeService.getRecipeIdFromRecipeCode(recipeCode);

        if(id==-1) {
           throw new RuntimeException("code is wrong");
       }
       return id;
    }
    private Integer getChefIdFromCode(String recipeCode) {
        int id= recipeService.getUserIdFromRecipeCode(recipeCode);
        if(id==-1) {
            throw new RuntimeException("code is wrong");
        }
        return id;
    }
}
