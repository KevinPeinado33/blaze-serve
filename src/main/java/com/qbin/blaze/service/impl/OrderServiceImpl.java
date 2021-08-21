package com.qbin.blaze.service.impl;

import com.qbin.blaze.models.Order;
import com.qbin.blaze.repository.IOrderDao;
import com.qbin.blaze.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

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

}
