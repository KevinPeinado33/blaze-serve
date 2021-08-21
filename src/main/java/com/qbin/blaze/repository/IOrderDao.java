package com.qbin.blaze.repository;

import com.qbin.blaze.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IOrderDao extends MongoRepository<Order, String> { }
