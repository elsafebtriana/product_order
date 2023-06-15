package com.test.product_order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true,value = {"created","updated", "deleted", "hibernate_lazy_initializer"})
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 5)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String customerName;

    @Column(length = 100)
    private String address;

    @Column(length = 20)
    private String city;
}
