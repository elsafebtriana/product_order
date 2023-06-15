package com.test.product_order.repository;

import com.test.product_order.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByDeletedIsNull();
    Product findByIdAndDeletedIsNull(Integer id);
}
