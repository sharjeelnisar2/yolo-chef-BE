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
        private Long total_price;
        private String currency_code;
        private String order_code;
        private String customer_contact_number;
        private String customer_name;
        private AddressDTO address;
        private List<OrderItemDTO> order_items;
    }

    @Getter
    @Setter
    public static class AddressDTO {
        private String house;
        private String street;
        private String area;
        private String zip_code;
        private String city;
        private String country;
    }

    @Getter
    @Setter
    public static class OrderItemDTO {
        private Long quantity;
        private Long price;
        private String recipe_code;
    }
}

