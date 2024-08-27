package com.yolo.chef.order;

import com.yolo.chef.order.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

//    @Query("SELECT new com.yolo.chef.order.dto.OrderResponse(o.id, o.price, o.currencyCode, c.name, s.value, o.createdAt) " +
//            "FROM Order o " +
//            "JOIN OrderItem oi ON o.id = oi.orderId " +
//            "JOIN Recipe r ON oi.recipeId = r.id " +
//            "JOIN User c ON r.userId = c.id " +
//            "JOIN OrderStatus s ON o.orderStatusId = s.id " +
//            "WHERE c.id = :userId " +
//            "AND (:orderStatus IS NULL OR s.value = :orderStatus) " +
//            "AND (:minPrice IS NULL OR o.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR o.price <= :maxPrice) " +
//            "AND (:search IS NULL OR o.customerContactNumber LIKE %:search% OR o.code LIKE %:search%)")

    @Query(value = "SELECT o.id, o.price, o.code, o.code, o.code, o.created_at " +
            "FROM `order` o ",nativeQuery = true)
    Page<?> findOrdersByChefId(
                                           Pageable pageable);





}

