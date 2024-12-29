package com.nnmfashion.clothingstore.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class OrderedProduct {

    @EmbeddedId
    private OrderedProductId id = new OrderedProductId(); // Initialize the composite key

    @ManyToOne
    @MapsId("orderId") // Maps the orderId attribute of OrderedProductId
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("productId") // Maps the productId attribute of OrderedProductId
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    // Getter and Setter for id
    public OrderedProductId getId() {
        return id;
    }

    public void setId(OrderedProductId id) {
        this.id = id;
    }

    // Getter and Setter for order
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        if (order != null) {
            this.id.setOrderId(order.getId()); // Set the orderId in the composite key
        }
    }

    // Getter and Setter for product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.id.setProductId(product.getId()); // Set the productId in the composite key
        }
    }

    // Getter and Setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
