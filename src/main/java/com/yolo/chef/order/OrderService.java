package com.yolo.chef.order;

import com.yolo.chef.order.Order;
import com.yolo.chef.order.OrderRepository;
import com.yolo.chef.address.Address;
import com.yolo.chef.address.AddressRepository;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.order.dto.OrderResponse;
import com.yolo.chef.orderItem.OrderItem;
import com.yolo.chef.orderItem.OrderItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public Page<?> getOrdersByChefId(Integer userId, int page, int size, String orderStatus, Double minPrice, Double maxPrice, String search) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findOrdersByChefId( pageable);
    }

    // Mock method for getting recipe ID from code
    private Integer getRecipeIdFromCode(String recipeCode) {
        return 1; // Example
    }
}
