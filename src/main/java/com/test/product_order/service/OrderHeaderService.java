package com.test.product_order.service;

import com.test.product_order.dto.OrderDetailDto;
import com.test.product_order.dto.OrderHeaderDto;
import com.test.product_order.entity.Customer;
import com.test.product_order.entity.OrderDetail;
import com.test.product_order.entity.OrderHeader;
import com.test.product_order.entity.Product;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.repository.CustomerRepository;
import com.test.product_order.repository.OrderDetailRepository;
import com.test.product_order.repository.OrderHeaderRepository;
import com.test.product_order.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class OrderHeaderService {
    private final OrderHeaderRepository orderHeaderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderHeaderService(OrderHeaderRepository orderHeaderRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<OrderHeader> getListOrderHeader(){
        return orderHeaderRepository.findByDeletedIsNullOrderById();
    }

    public OrderHeader getOrderHeaderById(Integer id){
        OrderHeader findOrderHeader = orderHeaderRepository.findByIdAndDeletedIsNull(id);
        if (findOrderHeader == null) throw new BadRequest("Order id is not found");
        return findOrderHeader;
    }

    public List<OrderHeader> findByContainingOrderNo(String orderNo){
        return orderHeaderRepository.findByOrderNoContainingIgnoreCaseAndDeletedIsNullOrderById(orderNo);
    }

    public List<OrderHeader> findByCustomerId(Integer customerId){
        Customer findCustomer = customerRepository.findById(customerId).orElse(null);
        if (findCustomer == null) throw new BadRequest("Customer id is not found");
        return orderHeaderRepository.findByCustomerIdAndDeletedIsNullOrderById(customerId);
    }

    public void deleteOrder(Integer orderHeaderId){
        OrderHeader findOrderHeader = orderHeaderRepository.findByIdAndDeletedIsNull(orderHeaderId);
        if (findOrderHeader == null) throw new BadRequest("Order id is not found");
        findOrderHeader.setDeleted(new Date());
        orderHeaderRepository.save(findOrderHeader);
        List<OrderDetail> findOrderDetail = orderDetailRepository.findByOrderAndDeletedIsNull(findOrderHeader);
        for (OrderDetail orderDetail: findOrderDetail){
            orderDetail.setDeleted(new Date());
            orderDetailRepository.save(orderDetail);
        }
        log.info(String.format("Delete order with id: %s", orderHeaderId));
    }

    public String generateOrderNo(Integer countOrder, String customerId) {
        String orderNo = "";
        if (orderNo.length() <= 1){
            orderNo = "0000"+countOrder;
        }else if (orderNo.length() <= 2){
            orderNo = "000"+countOrder;
        }else if (orderNo.length() <=3){
            orderNo = "00"+countOrder;
        }else if (orderNo.length() <=4){
            orderNo = "0"+countOrder;
        }else{
            orderNo = ""+countOrder;
        }
        String number = customerId+orderNo;
        return number;
    }

    public OrderHeader newOrder(OrderHeader orderHeaderRequest) {
        OrderHeader orderHeader = new OrderHeader();

        orderHeader.setOrderDate(orderHeaderRequest.getOrderDate());
        if (orderHeaderRequest.getRequiredDate() != null){
            orderHeader.setRequiredDate(orderHeaderRequest.getRequiredDate());
        }
        if (!StringUtils.isEmpty(orderHeaderRequest.getShipName())){
            orderHeader.setShipName(orderHeaderRequest.getShipName());
        }

        Customer findCustomer = customerRepository.findByIdAndDeletedIsNull(orderHeaderRequest.getCustomer().getId());
        if (findCustomer == null) throw new BadRequest("Customer id is not found");
        orderHeader.setCustomer(findCustomer);

        List<OrderHeader> orderHeaderList = orderHeaderRepository.findByDeletedIsNullOrderById();
        Integer countOrderHeader = orderHeaderList.size();
        orderHeader.setOrderNo(generateOrderNo(countOrderHeader+1, findCustomer.getCustomerName().substring(0,4)));
        orderHeaderRepository.save(orderHeader);

        Double totalPrice=0d;
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (orderHeaderRequest.getOrderDetail() != null && orderHeaderRequest.getOrderDetail().size() > 0){
            for (OrderDetail orderDetailRequest: orderHeaderRequest.getOrderDetail()){
                OrderDetail orderDetail = new OrderDetail();

                if (orderDetailRequest.getProduct().getId() == null) throw new BadRequest("Product id is empty");
                Product findProduct = productRepository.findByIdAndDeletedIsNull(orderDetailRequest.getProduct().getId());
                if (findProduct == null) throw new BadRequest("Product id is not found");

                orderDetail.setProduct(findProduct);

                if (orderDetailRequest.getQuantity() == null) throw new BadRequest("Quantity is empty");
                orderDetail.setQuantity(orderDetailRequest.getQuantity());

                Double subTotal = orderDetail.getQuantity() * findProduct.getPrice();
                orderDetail.setSubTotal(subTotal);
                orderDetail.setOrder(orderHeader);
                orderDetails.add(orderDetail);
                totalPrice = totalPrice+subTotal;
            }
            orderHeader.setOrderDetail(orderDetails);
        }

        orderHeader.setTotalPrice(totalPrice);
        return orderHeaderRepository.save(orderHeader);
    }

    public OrderHeader modifyOrderHeader(Integer orderHeaderId, OrderHeader orderHeaderRequest) {
        OrderHeader findOrderHeader = orderHeaderRepository.findByIdAndDeletedIsNull(orderHeaderId);
        if (findOrderHeader == null) throw new BadRequest("Order id is not found");
        findOrderHeader.setOrderDate(orderHeaderRequest.getOrderDate());
        if (orderHeaderRequest.getRequiredDate() != null){
            findOrderHeader.setRequiredDate(orderHeaderRequest.getRequiredDate());
        }
        if (!StringUtils.isEmpty(orderHeaderRequest.getShipName())){
            findOrderHeader.setShipName(orderHeaderRequest.getShipName());
        }

        Customer findCustomer = customerRepository.findByIdAndDeletedIsNull(orderHeaderRequest.getCustomer().getId());
        if (findCustomer == null) throw new BadRequest("Customer id not found");
        findOrderHeader.setCustomer(findCustomer);

        Double totalPrice=0d;
        List<OrderDetail> orderDetails = new ArrayList<>();
        if (orderHeaderRequest.getOrderDetail() != null && orderHeaderRequest.getOrderDetail().size() > 0){
            for (OrderDetail orderDetailRequest: orderHeaderRequest.getOrderDetail()){
                OrderDetail orderDetail = new OrderDetail();
                if (orderDetailRequest.getId() != null){
                    orderDetail = orderDetailRepository.findByIdAndDeletedIsNull(orderDetailRequest.getId());
                    if (orderDetail == null) throw new BadRequest("Order detail id is not found");
                }

                if (orderDetailRequest.getProduct().getId() == null) throw new BadRequest("Product id is empty");
                Product findProduct = productRepository.findByIdAndDeletedIsNull(orderDetailRequest.getProduct().getId());
                if (findProduct == null) throw new BadRequest("Product id is not found");
                orderDetail.setProduct(findProduct);

                if (orderDetailRequest.getQuantity() == null) throw new BadRequest("Quantity is empty");
                orderDetail.setQuantity(orderDetailRequest.getQuantity());

                Double subTotal = orderDetail.getQuantity() * findProduct.getPrice();
                orderDetail.setSubTotal(subTotal);
                orderDetail.setOrder(findOrderHeader);
                orderDetails.add(orderDetail);
                totalPrice = totalPrice+subTotal;
            }
            findOrderHeader.setOrderDetail(orderDetails);
        }

        findOrderHeader.setTotalPrice(totalPrice);
        return orderHeaderRepository.save(findOrderHeader);
    }
}
