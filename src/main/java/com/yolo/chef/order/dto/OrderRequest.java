package com.yolo.chef.order.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull
    @Valid
    private OrderDTO order;

    @Getter
    @Setter
    public static class OrderDTO {
        @NotNull
        @Positive
        private Long total_price;

        @NotEmpty
        private String order_code;

        @NotEmpty
        private String customer_contact_number;

        @NotEmpty
        private String customer_name;

        @NotNull
        @Valid
        private AddressDTO address;

        @NotNull
        @Valid
        private List<OrderItemDTO> order_items;
    }

    @Getter
    @Setter
    public static class AddressDTO {
        @NotEmpty
        private String house;

        @NotEmpty
        private String street;

        @NotEmpty
        private String area;

        @NotEmpty
        private String zip_code;

        @NotEmpty
        private String city;

        @NotEmpty
        private String country;
    }

    @Getter
    @Setter
    public static class OrderItemDTO {
        @NotNull
        @Min(1)  // quantity must be greater than zero
        private Long quantity;

        @NotNull
        @Min(1)  // price must be greater than zero
        private Long price;

        @NotEmpty
        private String recipe_code;
    }
}
