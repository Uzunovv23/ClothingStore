package com.nnmfashion.clothingstore.repositories;

import com.nnmfashion.clothingstore.entities.Order;
import com.nnmfashion.clothingstore.entities.OrderedProduct;
import com.nnmfashion.clothingstore.entities.OrderedProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, OrderedProductId> {
    public OrderedProduct getOrderedProductById(OrderedProductId id);
    @Modifying
    @Query("DELETE FROM OrderedProduct op WHERE op.order = :order")
    void deleteAllByOrder(@Param("order") Order order);
}