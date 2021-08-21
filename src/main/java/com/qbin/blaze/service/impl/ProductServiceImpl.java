package com.qbin.blaze.service.impl;

import com.qbin.blaze.models.Product;
import com.qbin.blaze.repository.IProductDao;
import com.qbin.blaze.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productDao.insert(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(String id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        productDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProductsByState(String state) {
        return productDao.findAllByState(state);
    }

}
