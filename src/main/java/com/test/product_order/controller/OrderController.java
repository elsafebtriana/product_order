package com.test.product_order.controller;

import com.test.product_order.dto.OrderHeaderDto;
import com.test.product_order.entity.OrderHeader;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.service.OrderHeaderService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/order")
@Api(tags = "order")
@Slf4j
public class OrderController {
    private final OrderHeaderService orderHeaderService;

    public OrderController(OrderHeaderService orderHeaderService) {
        this.orderHeaderService = orderHeaderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<OrderHeader> getListOrderHeader(){
        return orderHeaderService.getListOrderHeader();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderHeader getOrderById(@PathVariable Integer id){
        return orderHeaderService.getOrderHeaderById(id);
    }

    @GetMapping(value = "/customer/{customerId:[\\d]+}")
    public List<OrderHeader> findByCustomerId(@PathVariable Integer customerId){
        return orderHeaderService.findByCustomerId(customerId);
    }

    @GetMapping(value = "/order-no/{orderNo}")
    public List<OrderHeader> findByCustomerId(@PathVariable String orderNo){
        return orderHeaderService.findByContainingOrderNo(orderNo);
    }

    @PostMapping(value = "/new")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderHeader newOrder(@RequestBody OrderHeader orderHeader){
        if (orderHeader.getCustomer().getId() == null){
            throw new BadRequest("Customer id is empty");
        }
        if (orderHeader.getOrderDate() == null){
            throw new BadRequest("Order date is empty");
        }
        if (orderHeader.getOrderDetail() == null){
            throw new BadRequest("Order detail is empty");
        }
        return orderHeaderService.newOrder(orderHeader);
    }

    @PutMapping(value = "/modify/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderHeader modifyOrder(@PathVariable Integer id, @RequestBody OrderHeader orderHeader){
        if (orderHeader.getCustomer().getId() == null){
            throw new BadRequest("Customer id is empty");
        }
        if (orderHeader.getOrderDate() == null){
            throw new BadRequest("Order date is empty");
        }
        if (orderHeader.getOrderDetail() == null){
            throw new BadRequest("Order detail is empty");
        }
        return orderHeaderService.modifyOrderHeader(id, orderHeader);
    }

    @DeleteMapping(value = "/delete/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteOrder(@PathVariable Integer id){
        orderHeaderService.deleteOrder(id);
        return "Delete order successfully with id = "+id;
    }
}
