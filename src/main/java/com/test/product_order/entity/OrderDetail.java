package com.test.product_order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true,value = {"created","updated", "deleted", "hibernate_lazy_initializer"})
public class OrderDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 10)
    private Integer id;

    @JsonIgnore
    @ManyToOne(targetEntity = OrderHeader.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderHeader order;

    @OneToOne(targetEntity = Product.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Product product;

    private Integer quantity;

    private Double subTotal;
}
