package com.test.product_order.controller;

import com.test.product_order.entity.Customer;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.service.CustomerService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customer")
@Api(tags = "customer")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Customer> getListCustomer(){
        return customerService.getListCustomer();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Customer getCustomerById(@PathVariable Integer id){
        return customerService.getCustomerById(id);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Customer addNewCustomer(@RequestBody Customer customer){
        if (StringUtils.isEmpty(customer.getCustomerName())){
            throw new BadRequest("Customer name is empty");
        }
        return customerService.addNewCustomer(customer);
    }

    @PutMapping(value = "/update/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody Customer customer){
        if (StringUtils.isEmpty(customer.getCustomerName())){
            throw new BadRequest("Customer name is empty");
        }
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping(value = "/delete/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCustomer(@PathVariable Integer id){
        customerService.deleteCustomer(id);
        return "Delete customer successfully with id = "+id;
    }
}
