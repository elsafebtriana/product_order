package com.test.product_order.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Integer orderDetailId;
    private Integer productId;
    private Integer quantity;
}
