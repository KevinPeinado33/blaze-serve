package com.qbin.blaze.service;

import com.qbin.blaze.models.Order;
import com.qbin.blaze.models.OrderDetail;

import java.util.List;

public interface IOrderService {

    List<Order> getAllOrders();

    Order createOrder(Order order);

    void deleteOrder(String id);

    Order getOrderById(String id);

    OrderDetail createOrderDetail(OrderDetail orderDetail);

    void deleteOrderDetail(String id);

}
