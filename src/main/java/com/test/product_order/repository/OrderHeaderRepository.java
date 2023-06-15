package com.test.product_order.repository;

import com.test.product_order.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Integer> {
    List<OrderHeader> findByDeletedIsNullOrderById();
    OrderHeader findByIdAndDeletedIsNull(Integer id);
    List<OrderHeader> findByOrderNoContainingIgnoreCaseAndDeletedIsNullOrderById(String orderNo);
    List<OrderHeader> findByCustomerIdAndDeletedIsNullOrderById(Integer customerId);
}
