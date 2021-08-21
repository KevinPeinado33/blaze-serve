package com.qbin.blaze.service;

import com.qbin.blaze.models.Order;

import java.util.List;

public interface IOrderService {

    List<Order> getAllOrders();

    Order createOrder(Order order);

    void deleteOrder(String id);

}
