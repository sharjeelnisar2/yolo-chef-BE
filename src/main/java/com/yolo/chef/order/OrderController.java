package com.yolo.chef.order;

import com.yolo.chef.exception.UnauthorizedException;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.response.ErrorResponse;
import com.yolo.chef.user.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
@Autowired
private UserService userService;
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderRequest.OrderDTO orderDTO = orderRequest.getOrder();

        // Manual validation
        if (orderDTO.getTotal_price() == null || orderDTO.getTotal_price() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Validation error", "Total price must be greater than zero."));
        }
        if (orderDTO.getOrder_code() == null || orderDTO.getOrder_code().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Validation error", "Order code must not be empty."));
        }
        if (orderDTO.getAddress() == null || orderDTO.getAddress().getZip_code() == null
                || orderDTO.getAddress().getZip_code().length() != 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Validation error", "Zip code must be 5 digits."));
        }
        // Validate order_items
        if (orderDTO.getOrder_items() == null || orderDTO.getOrder_items().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Validation error", "Order items must not be empty."));
        }
        for (OrderRequest.OrderItemDTO itemDTO : orderDTO.getOrder_items()) {
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Validation error", "Quantity must be greater than zero."));
            }
            if (itemDTO.getPrice() == null || itemDTO.getPrice() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Validation error", "Price must be greater than zero."));
            }
        }

        // If all validations pass, proceed with saving the order
        try {
            orderService.saveOrder(orderDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Order submitted successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error", e.getMessage()));
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_ORDERS')")
    @GetMapping
    public ResponseEntity<?> getOrdersByChefId(
            @RequestParam(value = "order_status", required = false) Integer orderStatusId,
            @RequestParam(value = "min_price", required = false) Long minPrice,
            @RequestParam(value = "max_price", required = false) Long maxPrice,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication != null) {
                if (authentication instanceof JwtAuthenticationToken) {
                    JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                    Jwt jwt = (Jwt) jwtAuth.getPrincipal();
                    // Extract specific claims from the Jwt
                    String username = jwt.getClaim("preferred_username"); // Adjust based on your JWT structure
                    Integer userId= userService.getUserIdByUsername(username);

                    Map<String, Object> orders = orderService.getOrdersByChefId(userId, orderStatusId, minPrice, maxPrice, search, pageable);
                    return ResponseEntity.ok(orders);
                } else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("no user exist", "Unauthorized access"));
                }

            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("no user exist", "Unauthorized access"));
            }


        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage(), "Unauthorized access"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), "Internal server error"));
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_ORDER_DETAIL')")
    @GetMapping("/{order_id}")
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}

