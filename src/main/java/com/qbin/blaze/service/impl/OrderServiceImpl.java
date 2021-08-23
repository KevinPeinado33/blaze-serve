package com.qbin.blaze.service.impl;

import com.qbin.blaze.models.Order;
import com.qbin.blaze.models.OrderDetail;
import com.qbin.blaze.repository.IOrderDao;
import com.qbin.blaze.repository.IOrderDetailDao;
import com.qbin.blaze.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderDetailDao orderDetailDao;

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        return orderDao.insert(order);
    }

    @Override
    @Transactional
    public void deleteOrder(String id) { orderDao.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(String id) {
        return orderDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailDao.insert(orderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(String id) {
        orderDetailDao.deleteById(id);
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        return orderDao.save(order);
    }

}
