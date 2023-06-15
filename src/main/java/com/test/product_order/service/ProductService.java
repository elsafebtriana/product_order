package com.test.product_order.service;

import com.test.product_order.entity.Product;
import com.test.product_order.exception.BadRequest;
import com.test.product_order.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getListProduct() {
        return productRepository.findByDeletedIsNull();
    }

    public Product getProductById(Integer productId){
        Product findProduct = productRepository.findByIdAndDeletedIsNull(productId);
        if (findProduct == null) throw new BadRequest("Product id is not found");
        return findProduct;
    }

    public Product addNewProduct(Product newProduct){
        Product product = new Product();
        product.setProductName(newProduct.getProductName());
        product.setPrice(newProduct.getPrice());
        return productRepository.save(product);
    }

    public Product updateProduct(Integer productId, Product updateProduct){
        Product findProduct = productRepository.findByIdAndDeletedIsNull(productId);
        if (findProduct == null) throw new BadRequest("Product id is not found");
        findProduct.setProductName(updateProduct.getProductName());
        findProduct.setPrice(updateProduct.getPrice());
        log.info(String.format("Update product with id: %s",productId));
        return productRepository.save(findProduct);
    }

    public void deleteProduct(Integer productId){
        Product findProduct = productRepository.findByIdAndDeletedIsNull(productId);
        if (findProduct == null) throw new BadRequest("Product id is not found");
        findProduct.setDeleted(new Date());
        productRepository.save(findProduct);
        log.info(String.format("Delete product with id: %s", productId));
    }
}
