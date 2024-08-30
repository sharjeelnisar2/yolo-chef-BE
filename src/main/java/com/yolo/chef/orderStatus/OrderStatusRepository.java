package com.yolo.chef.orderStatus;

import com.yolo.chef.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
