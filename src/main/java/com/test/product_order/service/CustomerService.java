package com.test.product_order.service;

import com.test.product_order.entity.Customer;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getListCustomer() {
        return customerRepository.findByDeletedIsNull();
    }

    public Customer getCustomerById(Integer customerId){
        Customer findCustomer = customerRepository.findByIdAndDeletedIsNull(customerId);
        if (findCustomer == null) throw new BadRequest("Customer id is not found");
        return findCustomer;
    }

    public Customer addNewCustomer(Customer newCustomer){
        Customer customer = new Customer();
        customer.setCustomerName(newCustomer.getCustomerName());
        if (!StringUtils.isEmpty(newCustomer.getAddress())){
            customer.setAddress(newCustomer.getAddress());
        }
        if (!StringUtils.isEmpty(newCustomer.getCity())){
            customer.setCity(newCustomer.getCity());
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer customerId, Customer updateCustomer){
        Customer findCustomer = customerRepository.findByIdAndDeletedIsNull(customerId);
        if (findCustomer == null) throw new BadRequest("Customer id is not found");
        findCustomer.setCustomerName(updateCustomer.getCustomerName());
        if (!StringUtils.isEmpty(updateCustomer.getAddress())){
            findCustomer.setAddress(updateCustomer.getAddress());
        }
        if (!StringUtils.isEmpty(updateCustomer.getCity())){
            findCustomer.setCity(updateCustomer.getCity());
        }
        log.info(String.format("Update customer with id: %s",customerId));
        return customerRepository.save(findCustomer);
    }

    public void deleteCustomer(Integer customerId){
        Customer findCustomer = customerRepository.findByIdAndDeletedIsNull(customerId);
        if (findCustomer == null) throw new BadRequest("Customer id is not found");
        findCustomer.setDeleted(new Date());
        customerRepository.save(findCustomer);
        log.info(String.format("Delete customer with id: %s", customerId));
    }
}
