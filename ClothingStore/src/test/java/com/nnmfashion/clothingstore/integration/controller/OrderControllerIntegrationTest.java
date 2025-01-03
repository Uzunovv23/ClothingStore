package com.nnmfashion.clothingstore.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnmfashion.clothingstore.dtos.OrderDto;
import com.nnmfashion.clothingstore.dtos.OrderedProductDto;
import com.nnmfashion.clothingstore.entities.*;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import com.nnmfashion.clothingstore.repositories.OrderRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepo.deleteAll();
        clientRepo.deleteAll();
        productRepo.deleteAll();
    }

    @Test
    void testGetAllOrders() throws Exception {
        Client client = createTestClient("John Doe");
        Product product = createTestProduct("T-Shirt", 19.99);

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDate.now());
        order.setOrderedProducts(Set.of(createTestOrderedProduct(order, product, 2)));

        orderRepo.save(order);

        mockMvc.perform(get("/api/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].client.name").value("John Doe"))
                .andExpect(jsonPath("$[0].orderedProducts[0].product.name").value("T-Shirt"));
    }

    @Test
    void testGetOrderById() throws Exception {
        Client client = createTestClient("Jane Smith");
        Product product = createTestProduct("Sweater", 29.99);

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDate.now());
        order.setOrderedProducts(Set.of(createTestOrderedProduct(order, product, 3)));

        Order savedOrder = orderRepo.save(order);

        mockMvc.perform(get("/api/orders/{id}", savedOrder.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.client.name").value("Jane Smith"))
                .andExpect(jsonPath("$.orderedProducts[0].product.name").value("Sweater"))
                .andExpect(jsonPath("$.orderedProducts[0].quantity").value(3));
    }

    @Test
    void testCreateOrder() throws Exception {
        Client client = createTestClient("Alice Johnson");
        Product product = createTestProduct("Hat", 14.99);

        OrderedProductDto orderedProductDto = new OrderedProductDto();
        orderedProductDto.setProductId(product.getId());
        orderedProductDto.setQuantity(2);

        OrderDto orderDto = new OrderDto();
        orderDto.setClientId(client.getId());
        orderDto.setOrderDate(LocalDate.now());
        orderDto.setOrderedProducts(Set.of(orderedProductDto));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.client.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.orderedProducts[0].product.name").value("Hat"))
                .andExpect(jsonPath("$.orderedProducts[0].quantity").value(2));
    }

    @Test
    void testUpdateOrder() throws Exception {
        Client client = createTestClient("Bob Williams");
        Product product1 = createTestProduct("Jacket", 49.99);
        Product product2 = createTestProduct("Shoes", 89.99);

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDate.now());
        order.setOrderedProducts(Set.of(createTestOrderedProduct(order, product1, 1)));

        Order savedOrder = orderRepo.save(order);

        OrderedProductDto updatedOrderedProduct = new OrderedProductDto();
        updatedOrderedProduct.setProductId(product2.getId());
        updatedOrderedProduct.setQuantity(2);

        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setClientId(client.getId());
        updatedOrderDto.setOrderDate(LocalDate.now());
        updatedOrderDto.setOrderedProducts(Set.of(updatedOrderedProduct));

        mockMvc.perform(put("/api/orders/{id}", savedOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderedProducts[0].product.name").value("Shoes"))
                .andExpect(jsonPath("$.orderedProducts[0].quantity").value(2));
    }

    @Test
    void testDeleteOrder() throws Exception {
        Client client = createTestClient("Charlie Brown");
        Product product = createTestProduct("Cap", 9.99);

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDate.now());
        order.setOrderedProducts(Set.of(createTestOrderedProduct(order, product, 4)));

        Order savedOrder = orderRepo.save(order);

        mockMvc.perform(delete("/api/orders/{id}", savedOrder.getId()))
                .andExpect(status().isOk());

        assertThat(orderRepo.findById(savedOrder.getId())).isEmpty();
    }

    private Client createTestClient(String name) {
        Client client = new Client();
        client.setName(name);
        return clientRepo.save(client);
    }

    private Product createTestProduct(String name, double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productRepo.save(product);
    }

    private OrderedProduct createTestOrderedProduct(Order order, Product product, int quantity) {
        Product managedProduct = productRepo.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setOrder(order);
        orderedProduct.setProduct(managedProduct);
        orderedProduct.setQuantity(quantity);

        if (order.getOrderedProducts() == null) {
            order.setOrderedProducts(new HashSet<>());
        }
        order.getOrderedProducts().add(orderedProduct);

        return orderedProduct;
    }
}