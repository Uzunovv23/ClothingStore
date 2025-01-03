package com.nnmfashion.clothingstore.unit.controller;

import com.nnmfashion.clothingstore.controllers.OrderController;
import com.nnmfashion.clothingstore.dtos.OrderDto;
import com.nnmfashion.clothingstore.entities.Client;
import com.nnmfashion.clothingstore.entities.Order;
import com.nnmfashion.clothingstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
public class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private Order order;
    private OrderDto orderDto;
    private Client client;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        // Mock Client
        client = new Client();
        client.setId(1L);
        client.setName("Alice Johnson");
        client.setEmail("alice.johnson@example.com");

        // Mock Order
        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setOrderDate(LocalDate.of(2024, 12, 1));

        // Mock OrderDto
        orderDto = new OrderDto();
        orderDto.setClientId(1L); // Assuming OrderDto includes a clientId field
        orderDto.setOrderDate(LocalDate.of(2024, 12, 1));
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<Order> orders = Arrays.asList(order);

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].client.name", is("Alice Johnson")))
                .andExpect(jsonPath("$[0].orderDate", is(Arrays.asList(2024, 12, 1))));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.name", is("Alice Johnson")))
                .andExpect(jsonPath("$.orderDate", is(Arrays.asList(2024, 12, 1))));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\": 1, \"orderDate\": \"2024-12-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.name", is("Alice Johnson")))
                .andExpect(jsonPath("$.orderDate", is(Arrays.asList(2024, 12, 1))));

        verify(orderService, times(1)).createOrder(any(OrderDto.class));
    }

    @Test
    void testUpdateOrder() throws Exception {
        when(orderService.updateOrder(eq(1L), any(OrderDto.class))).thenReturn(order);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\": 1, \"orderDate\": \"2024-12-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.name", is("Alice Johnson")))
                .andExpect(jsonPath("$.orderDate", is(Arrays.asList(2024, 12, 1))));

        verify(orderService, times(1)).updateOrder(eq(1L), any(OrderDto.class));
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrderById(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).deleteOrderById(1L);
    }
}
