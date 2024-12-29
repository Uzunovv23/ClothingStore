package com.nnmfashion.clothingstore.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate orderDate;

    // One-to-many relationship with OrderedProduct
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderedProduct> orderedProducts = new HashSet<>();

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for client
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Getter and Setter for orderDate
    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    // Getter and Setter for orderedProducts
    public Set<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProduct> orderedProducts) {
        // Clear existing products to avoid dangling references
        this.orderedProducts.clear();

        // Maintain bidirectional relationships
        if (orderedProducts != null) {
            this.orderedProducts.addAll(orderedProducts);
            orderedProducts.forEach(product -> product.setOrder(this));
        }
    }
}
