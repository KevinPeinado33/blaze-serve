package com.qbin.blaze.service;

import com.qbin.blaze.models.Order;
import com.qbin.blaze.models.Product;

import java.util.List;

public interface IProductService {

    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product findProductById(String id);

    Product updateProduct(Product product);

    void deleteProduct(String id);

    List<Product> getAllProductsByState(String state);

}
