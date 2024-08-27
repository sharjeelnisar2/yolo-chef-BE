package com.yolo.chef.order.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private OrderDTO order;

    @Getter
    @Setter
    public static class OrderDTO {
        private Long totalPrice;
        private String currencyCode;
        private String orderCode;
        private String customerContactNumber;
        private AddressDTO address;
        private List<OrderItemDTO> orderItems;
    }

    @Getter
    @Setter
    public static class AddressDTO {
        private String house;
        private String street;
        private String area;
        private String zipCode;
        private String city;
        private String country;
    }

    @Getter
    @Setter
    public static class OrderItemDTO {
        private Long quantity;
        private Long price;
        private String recipeCode;
    }
}

