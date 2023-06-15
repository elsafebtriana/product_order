package com.test.product_order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderHeaderDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requiredDate;

    private String shipName;

    private Integer customerId;

    private List<OrderDetailDto> orderDetailDtoList;
}
