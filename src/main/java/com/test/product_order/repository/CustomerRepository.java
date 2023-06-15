package com.test.product_order.repository;

import com.test.product_order.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByDeletedIsNull();
    Customer findByIdAndDeletedIsNull(Integer id);
}
