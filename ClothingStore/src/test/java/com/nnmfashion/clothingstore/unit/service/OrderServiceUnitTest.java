package com.nnmfashion.clothingstore.unit.service;

import com.nnmfashion.clothingstore.dtos.OrderDto;
import com.nnmfashion.clothingstore.dtos.OrderedProductDto;
import com.nnmfashion.clothingstore.entities.*;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import com.nnmfashion.clothingstore.repositories.OrderRepository;
import com.nnmfashion.clothingstore.repositories.OrderedProductRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import com.nnmfashion.clothingstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private OrderedProductRepository orderedProductRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepo.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepo, times(1)).findById(1L);
    }

    @Test
    public void testGetOrderById_NotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(1L));
        verify(orderRepo, times(1)).findById(1L);
    }

    @Test
    public void testCreateOrder() {
        // Mock input DTO
        OrderedProductDto orderedProductDto = new OrderedProductDto();
        orderedProductDto.setProductId(1L);
        orderedProductDto.setQuantity(2);

        OrderDto orderDto = new OrderDto();
        orderDto.setClientId(1L);
        orderDto.setOrderDate(LocalDate.now());
        orderDto.setOrderedProducts(Set.of(orderedProductDto));

        // Mock related entities
        Client client = new Client();
        client.setId(1L);
        when(clientRepo.getClientById(1L)).thenReturn(client);

        Product product = new Product();
        product.setId(1L);
        when(productRepo.getProductById(1L)).thenReturn(product);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        when(orderRepo.save(any(Order.class))).thenReturn(savedOrder);

        // Test
        Order result = orderService.createOrder(orderDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clientRepo, times(1)).getClientById(1L);
        verify(productRepo, times(1)).getProductById(1L);
        verify(orderRepo, times(2)).save(any(Order.class));
        verify(orderedProductRepo, times(1)).save(any(OrderedProduct.class));
    }

    @Test
    public void testUpdateOrder() {
        // Mock existing order
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(existingOrder));

        // Mock input DTO
        OrderedProductDto orderedProductDto = new OrderedProductDto();
        orderedProductDto.setProductId(2L);
        orderedProductDto.setQuantity(3);

        OrderDto orderDto = new OrderDto();
        orderDto.setClientId(1L);
        orderDto.setOrderDate(LocalDate.now());
        orderDto.setOrderedProducts(Set.of(orderedProductDto));

        // Mock related entities
        Client client = new Client();
        client.setId(1L);
        when(clientRepo.getClientById(1L)).thenReturn(client);

        Product product = new Product();
        product.setId(2L);
        when(productRepo.getProductById(2L)).thenReturn(product);

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        when(orderRepo.save(any(Order.class))).thenReturn(updatedOrder);

        // Test
        Order result = orderService.updateOrder(1L, orderDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepo, times(1)).findById(1L);
        verify(clientRepo, times(1)).getClientById(1L);
        verify(productRepo, times(1)).getProductById(2L);
        verify(orderedProductRepo, times(1)).deleteAllByOrder(existingOrder);
        verify(orderedProductRepo, times(1)).save(any(OrderedProduct.class));
        verify(orderRepo, times(1)).save(any(Order.class));
    }

    @Test
    public void testDeleteOrderById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrderById(1L);

        verify(orderRepo, times(1)).findById(1L);
        verify(orderRepo, times(1)).delete(order);
    }

    @Test
    public void testDeleteOrderById_NotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.deleteOrderById(1L));
        verify(orderRepo, times(1)).findById(1L);
    }
}
