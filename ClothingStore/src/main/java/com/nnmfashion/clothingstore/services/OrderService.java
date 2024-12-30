package com.nnmfashion.clothingstore.services;

import com.nnmfashion.clothingstore.dtos.OrderDto;
import com.nnmfashion.clothingstore.dtos.OrderedProductDto;
import com.nnmfashion.clothingstore.entities.*;
import com.nnmfashion.clothingstore.repositories.ClientRepository;
import com.nnmfashion.clothingstore.repositories.OrderRepository;
import com.nnmfashion.clothingstore.repositories.OrderedProductRepository;
import com.nnmfashion.clothingstore.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final ClientRepository clientRepo;
    private final ProductRepository productRepo;
    private final OrderedProductRepository orderedProductRepo;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, ProductRepository productRepository, OrderedProductRepository orderedProductRepository) {
        this.orderRepo = orderRepository;
        this.clientRepo = clientRepository;
        this.productRepo = productRepository;
        this.orderedProductRepo = orderedProductRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));
    }

    public Order createOrder(OrderDto orderDto) {
        // Fetch the Client by ID
        Client client = clientRepo.getClientById(orderDto.getClientId());
        if (client == null) {
            throw new NoSuchElementException("Client not found");
        }

        // Create and save the order
        Order newOrder = new Order();
        newOrder.setClient(client);
        newOrder.setOrderDate(orderDto.getOrderDate());
        newOrder = orderRepo.save(newOrder);

        Set<OrderedProduct> orderedProducts = new HashSet<>();
        for (OrderedProductDto orderedProductDto : orderDto.getOrderedProducts()) {
            // Fetch the Product by ID
            Product product = productRepo.getProductById(orderedProductDto.getProductId());
            if (product == null) {
                throw new NoSuchElementException("Product not found");
            }

            // Create and set OrderedProductId
            OrderedProductId orderedProductId = new OrderedProductId();
            orderedProductId.setOrderId(newOrder.getId());
            orderedProductId.setProductId(product.getId());

            // Create and set OrderedProduct
            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setId(orderedProductId);
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(orderedProductDto.getQuantity());
            orderedProduct.setOrder(newOrder);

            orderedProductRepo.save(orderedProduct);
            orderedProducts.add(orderedProduct);
        }

        newOrder.setOrderedProducts(orderedProducts);
        return orderRepo.save(newOrder);
    }

    @Transactional
    public Order updateOrder(Long orderId, OrderDto orderDto) {
        // Fetch the existing order
        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        // Update order details
        Client client = clientRepo.getClientById(orderDto.getClientId());
        if (client == null) {
            throw new NoSuchElementException("Client not found");
        }
        existingOrder.setClient(client);
        existingOrder.setOrderDate(orderDto.getOrderDate());

        // Clear existing OrderedProducts for this order
        orderedProductRepo.deleteAllByOrder(existingOrder);

        // Add updated OrderedProducts
        Set<OrderedProduct> updatedOrderedProducts = new HashSet<>();
        for (OrderedProductDto orderedProductDto : orderDto.getOrderedProducts()) {
            Product product = productRepo.getProductById(orderedProductDto.getProductId());
            if (product == null) {
                throw new NoSuchElementException("Product not found");
            }

            OrderedProductId orderedProductId = new OrderedProductId();
            orderedProductId.setOrderId(existingOrder.getId());
            orderedProductId.setProductId(product.getId());

            OrderedProduct orderedProduct = new OrderedProduct();
            orderedProduct.setId(orderedProductId);
            orderedProduct.setProduct(product);
            orderedProduct.setQuantity(orderedProductDto.getQuantity());
            orderedProduct.setOrder(existingOrder);

            orderedProductRepo.save(orderedProduct);
            updatedOrderedProducts.add(orderedProduct);
        }

        existingOrder.setOrderedProducts(updatedOrderedProducts);
        return orderRepo.save(existingOrder);
    }



    public void deleteOrderById(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));
        orderRepo.delete(order);
    }
}
