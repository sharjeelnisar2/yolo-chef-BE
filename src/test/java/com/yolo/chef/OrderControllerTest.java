package com.yolo.chef;

import com.yolo.chef.order.Order;
import com.yolo.chef.order.OrderService;
import com.yolo.chef.order.dto.OrderRequest;
import com.yolo.chef.response.ErrorResponse;
import com.yolo.chef.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    private OrderRequest orderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1);
        order.setPrice(100L);
        order.setCustomerName("John Doe");

        orderRequest = new OrderRequest();
//        orderRequest.setOrder(order);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateOrder() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"order\": { \"price\": 100, \"customerName\": \"John Doe\" }}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Order submitted successfully\"}"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_VIEW_ORDERS")
    void testGetOrdersByChefId() throws Exception {
        Map<String, Object> orders = new HashMap<>();
        orders.put("current_page", 1);
        orders.put("page_size", 10);
        orders.put("total_items", 100);
        orders.put("total_pages", 10);

        when(orderService.getOrdersByChefId(any(Integer.class), any(), any(), any(), any(), any())).thenReturn(orders);
        when(userService.getUserIdByUsername(any(String.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"current_page\": 1, \"page_size\": 10, \"total_items\": 100, \"total_pages\": 10 }"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_VIEW_ORDER_DETAIL")
    void testGetOrderDetails() throws Exception {
        when(orderService.getOrderDetails(any(Integer.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/detail/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"order_id\": 1, \"price\": 100, \"customer_name\": \"John Doe\"}"));
    }
}

