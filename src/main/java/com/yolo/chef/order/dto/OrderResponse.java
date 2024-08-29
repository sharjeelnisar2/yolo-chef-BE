package com.yolo.chef.order.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private Integer order_id;
    private Long total_price;
    private String customer_name;
    private String status;
    private LocalDateTime created_at;

    public OrderResponse() {
        // Default constructor
    }

    public OrderResponse(Integer order_id, Long total_price, String customer_name, String status, LocalDateTime created_at) {
        this.order_id = order_id;
        this.total_price = total_price;
        this.customer_name = customer_name;
        this.status = status;
        this.created_at = created_at;
    }
}
