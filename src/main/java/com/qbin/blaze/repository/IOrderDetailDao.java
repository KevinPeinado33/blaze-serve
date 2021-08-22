package com.qbin.blaze.repository;

import com.qbin.blaze.models.OrderDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IOrderDetailDao extends MongoRepository<OrderDetail, String> { }
