package com.nnmfashion.clothingstore.integration.service;

import com.nnmfashion.clothingstore.dtos.OrderDto;
import com.nnmfashion.clothingstore.dtos.OrderedProductDto;
import com.nnmfashion.clothingstore.entities.*;
import com.nnmfashion.clothingstore.repositories.*;
import com.nnmfashion.clothingstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    private Client testClient;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        orderedProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        clientRepository.deleteAll();

        // Create test Client
        testClient = new Client();
        testClient.setName("Test Client");
        testClient.setEmail("testclient@example.com");
        testClient = clientRepository.save(testClient);

        // Create test Product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void testGetAllOrders() {
        Order order = new Order();
        order.setClient(testClient);
        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);

        List<Order> orders = orderService.getAllOrders();
        assertEquals(1, orders.size());
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setClient(testClient);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);

        Order fetchedOrder = orderService.getOrderById(savedOrder.getId());
        assertNotNull(fetchedOrder);
        assertEquals(savedOrder.getId(), fetchedOrder.getId());
        assertEquals(testClient.getId(), fetchedOrder.getClient().getId());
    }

    @Test
    void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setClientId(testClient.getId());
        orderDto.setOrderDate(LocalDate.now());

        OrderedProductDto orderedProductDto = new OrderedProductDto();
        orderedProductDto.setProductId(testProduct.getId());
        orderedProductDto.setQuantity(2);

        orderDto.setOrderedProducts(Set.of(orderedProductDto));

        Order createdOrder = orderService.createOrder(orderDto);
        assertNotNull(createdOrder.getId());
        assertEquals(1, createdOrder.getOrderedProducts().size());

        OrderedProduct orderedProduct = createdOrder.getOrderedProducts().iterator().next();
        assertEquals(testProduct.getId(), orderedProduct.getProduct().getId());
        assertEquals(2, orderedProduct.getQuantity());
    }

    @Test
    void testUpdateOrder() {
        // Create initial order
        Order initialOrder = new Order();
        initialOrder.setClient(testClient);
        initialOrder.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(initialOrder);

        // Prepare update DTO
        OrderDto updateDto = new OrderDto();
        updateDto.setClientId(testClient.getId());
        updateDto.setOrderDate(LocalDate.now().plusDays(1));

        OrderedProductDto updatedOrderedProduct = new OrderedProductDto();
        updatedOrderedProduct.setProductId(testProduct.getId());
        updatedOrderedProduct.setQuantity(5);

        updateDto.setOrderedProducts(Set.of(updatedOrderedProduct));

        // Perform update
        Order updatedOrder = orderService.updateOrder(savedOrder.getId(), updateDto);
        assertEquals(savedOrder.getId(), updatedOrder.getId());
        assertEquals(LocalDate.now().plusDays(1), updatedOrder.getOrderDate());
        assertEquals(1, updatedOrder.getOrderedProducts().size());

        OrderedProduct orderedProduct = updatedOrder.getOrderedProducts().iterator().next();
        assertEquals(testProduct.getId(), orderedProduct.getProduct().getId());
        assertEquals(5, orderedProduct.getQuantity());
    }

    @Test
    void testDeleteOrderById() {
        Order order = new Order();
        order.setClient(testClient);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);

        orderService.deleteOrderById(savedOrder.getId());
        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }
}
