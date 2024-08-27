package com.yolo.chef.order.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private Integer orderId;
    private Long totalPrice;
    private String currencyCode;
    private String customerName;
    private String orderStatus;
    private LocalDateTime createdAt;

    public OrderResponse(Integer orderId, Long totalPrice, String currencyCode, String customerName, String orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.currencyCode = currencyCode;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }
}



