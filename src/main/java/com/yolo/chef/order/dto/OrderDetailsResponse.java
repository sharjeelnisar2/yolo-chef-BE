package com.yolo.chef.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailsResponse {

    private OrderDTO order;

    @Getter
    @Setter
    public static class OrderDTO {
        private String customer_name;
        private Long total_price;
        private String currency_code;
        private String customer_contact_number;
        private AddressDTO address;
        private List<OrderItemDTO> order_items;

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
    }

    @Getter
    @Setter
    public static class OrderItemDTO {
        private String recipe_name;
        private Long quantity;
        private Long price;
        private Integer serving_size;
    }
}
