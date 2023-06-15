package com.test.product_order.controller;

import com.test.product_order.entity.Product;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.service.ProductService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/product")
@Api(tags = "product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Product> getListProduct(){
        return productService.getListProduct();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Product getProductById(@PathVariable Integer id){
        return productService.getProductById(id);
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Product addNewProduct(@RequestBody Product product){
        if (StringUtils.isEmpty(product.getProductName())){
            throw new BadRequest("Product name is empty");
        }
        if (product.getPrice() == null){
            throw new BadRequest("Price is empty");
        }
        return productService.addNewProduct(product);
    }

    @PutMapping(value = "/update/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product){
        if (StringUtils.isEmpty(product.getProductName())){
            throw new BadRequest("Product name is empty");
        }
        if (product.getPrice() == null){
            throw new BadRequest("Price is empty");
        }
        return productService.updateProduct(id, product);
    }

    @DeleteMapping(value = "/delete/{id:[\\d]+}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return "Delete product successfully with id = "+id;
    }
}
