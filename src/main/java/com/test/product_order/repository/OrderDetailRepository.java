package com.test.product_order.repository;

import com.test.product_order.entity.OrderDetail;
import com.test.product_order.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderAndDeletedIsNull(OrderHeader orderHeader);
    OrderDetail findByIdAndDeletedIsNull(Integer id);
}
