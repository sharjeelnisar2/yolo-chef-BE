package com.yolo.chef.order;

import com.yolo.chef.exception.UnauthorizedException;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            orderService.saveOrder(orderRequest.getOrder());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Order submitted successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_ORDERS')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByChefId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search) {

        try {
            Map<String, Object> orders = orderService.getOrdersByChefId(userId, page, size, orderStatus, minPrice, maxPrice, search);
            return ResponseEntity.ok(orders);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage(), "Unauthorized access"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), "Internal server error"));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_ORDER_DETAIL')")
    @GetMapping("/detail/{order_id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer order_id)
    {
        try
        {
            return ResponseEntity.ok(orderService.getOrderDetails(order_id));
        }
        catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage(), "Unauthorized access"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), "Internal server error"));
        }
    }
}

