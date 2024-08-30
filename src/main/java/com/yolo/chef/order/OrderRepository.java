package com.yolo.chef.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
//    @Query(value = "SELECT DISTINCT o.id, o.price, o.code, i.customer_name, s.value, o.created_at " +
//            "FROM `order` o " +
//            "JOIN order_item oi ON o.id = oi.order_id " +
//            "JOIN recipe r ON oi.recipe_id = r.id " +
//            "JOIN idea i ON r.idea_id = i.id " +
//            "JOIN order_status s ON o.order_status_id = s.id " +
//            "WHERE r.user_id = :userId " +
//            "AND (:orderStatus IS NULL OR s.value = :orderStatus) " +
//            "AND (:minPrice IS NULL OR o.price >= :minPrice) " +
//            "AND (:maxPrice IS NULL OR o.price <= :maxPrice) " +
//            "AND (:search IS NULL OR o.customer_contact_number LIKE %:search% OR o.code LIKE %:search%)"+
//            "GROUP BY o.id, o.price, o.code, i.customer_name, s.value, o.created_at",nativeQuery = true)
//    Page<Object[]> findOrdersByChefId(@Param("userId") Integer userId,
//                               @Param("orderStatus") String orderStatus,
//                               @Param("minPrice") Double minPrice,
//                               @Param("maxPrice") Double maxPrice,
//                               @Param("search") String search,
//                               Pageable pageable);
Page<Order> findByChefIdAndOrderStatusIdAndPriceBetweenAndCustomerContactNumberContainingOrCodeContaining(
        Integer chefId, Integer orderStatusId, Long minPrice, Long maxPrice,
        String customerContactNumber, String code, Pageable pageable);

    @Query(value = "SELECT o.price, o.customer_contact_number,o.customer_name,o.created_at,s.value ," +
            "a.house, a.street, a.area, a.zip_code, a.city, a.country, " +
            "r.name AS recipe_name, oi.quantity, oi.price AS item_price, r.serving_size " +
            "FROM `order` o " +
            "JOIN address a ON o.address_id = a.id " +
            "JOIN order_status s ON o.order_status_id = s.id " +
            "JOIN order_item oi ON o.id = oi.order_id " +
            "JOIN recipe r ON oi.recipe_id = r.id " +
            "WHERE o.id = :orderId",
            nativeQuery = true)
    List<Object[]> findOrderDetailsById(@Param("orderId") Integer orderId);

}

