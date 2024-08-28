package com.yolo.chef.order;

import com.yolo.chef.order.Order;
import com.yolo.chef.order.OrderRepository;
import com.yolo.chef.address.Address;
import com.yolo.chef.address.AddressRepository;
import com.yolo.chef.order.dto.OrderDetailsResponse;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.order.dto.OrderResponse;
import com.yolo.chef.orderItem.OrderItem;
import com.yolo.chef.orderItem.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public void saveOrder(OrderRequest.OrderDTO orderDTO) {
        Address address = new Address();
        address.setHouse(orderDTO.getAddress().getHouse());
        address.setStreet(orderDTO.getAddress().getStreet());
        address.setArea(orderDTO.getAddress().getArea());
        address.setZip_code(orderDTO.getAddress().getZipCode());
        address.setCity(orderDTO.getAddress().getCity());
        address.setCountry(orderDTO.getAddress().getCountry());
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        Address savedAddress = addressRepository.save(address);

        Order order = new Order();
        order.setPrice(orderDTO.getTotalPrice());
        order.setCode(orderDTO.getOrderCode());
        order.setCustomerContactNumber(orderDTO.getCustomerContactNumber());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setAddressId(savedAddress.getId());
        order.setOrderStatusId(1);
        Order savedOrder = orderRepository.save(order);

        for (OrderRequest.OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setRecipeId(getRecipeIdFromCode(itemDTO.getRecipeCode()));
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItemRepository.save(orderItem);
        }
    }


    public Map<String, Object> getOrdersByChefId(Integer userId, int page, int size, String orderStatus, Double minPrice, Double maxPrice, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> ordersPage = orderRepository.findOrdersByChefId(userId, orderStatus, minPrice, maxPrice, search, pageable);

        // Map raw data to OrderResponse DTOs
        List<OrderResponse> orderResponses = ordersPage.getContent().stream().map(objects -> {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrder_id((Integer) objects[0]);
            orderResponse.setTotal_price((Long) objects[1]);
            orderResponse.setCustomer_name((String) objects[3]);
            orderResponse.setStatus((String) objects[4]);
            orderResponse.setCreated_at(((Timestamp) objects[5]).toLocalDateTime());
            return orderResponse;
        }).collect(Collectors.toList());


        // Format the response
        return Map.of(
                "current_page", ordersPage.getNumber() + 1,
                "page_size", ordersPage.getSize(),
                "total_items", ordersPage.getTotalElements(),
                "total_pages", ordersPage.getTotalPages(),
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
        orderDTO.setCustomer_name((String) firstResult[2]);
        orderDTO.setTotal_price((Long) firstResult[0]);
        orderDTO.setCurrency_code("USD");
        orderDTO.setCustomer_contact_number((String) firstResult[1]);

        // Map the AddressDTO
        OrderDetailsResponse.OrderDTO.AddressDTO addressDTO = new OrderDetailsResponse.OrderDTO.AddressDTO();
        addressDTO.setHouse((String) firstResult[5]);
        addressDTO.setStreet((String) firstResult[6]);
        addressDTO.setArea((String) firstResult[7]);
        addressDTO.setZip_code((String) firstResult[8]);
        addressDTO.setCity((String) firstResult[9]);
        addressDTO.setCountry((String) firstResult[10]);
        // Set the AddressDTO in the OrderDTO
        orderDTO.setAddress(addressDTO);

        // Map the OrderItemDTOs
        List<OrderDetailsResponse.OrderItemDTO> orderItems = new ArrayList<>();
        for (Object[] result : results) {
            OrderDetailsResponse.OrderItemDTO orderItemDTO = new OrderDetailsResponse.OrderItemDTO();
            orderItemDTO.setRecipe_name((String) result[11]); // Assuming index 11 corresponds to recipe_name
            orderItemDTO.setQuantity((Long) result[12]); // Assuming index 12 corresponds to quantity
            orderItemDTO.setPrice((Long) result[13]); // Assuming index 13 corresponds to price
            orderItemDTO.setServing_size((Integer) result[14]); // Assuming index 14 corresponds to serving_size

            orderItems.add(orderItemDTO);
        }

        orderDTO.setOrder_items(orderItems);
        response.setOrder(orderDTO);

        return response;
    }

    // Mock method for getting recipe ID from code
    private Integer getRecipeIdFromCode(String recipeCode) {
        return 1; // Example
    }
}
