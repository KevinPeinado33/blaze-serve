package com.qbin.blaze.repository;

import com.qbin.blaze.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IProductDao extends MongoRepository<Product, String> {

    List<Product> findAllByState(String state);

}
