package com.nnmfashion.clothingstore.dtos;

import com.nnmfashion.clothingstore.entities.OrderedProduct;

import java.time.LocalDate;
import java.util.Set;

public class OrderDto {
    private Long clientId;
    private LocalDate orderDate;
    private Set<OrderedProductDto> orderedProducts;


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderedProductDto> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProductDto> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
}