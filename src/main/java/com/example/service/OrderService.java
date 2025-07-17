package com.example.service;

import com.example.entity.Order;
import com.example.entity.WatchOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userId, Order order);

    public List<WatchOrder> getOrdersByUser(Integer userId);

    public WatchOrder updateOrderStatus(Integer id, String orderStatus);

    public WatchOrder getOrdersByOrderId(String orderId);

    public Page<WatchOrder> getAllOrdersPagination(Integer pageSize, Integer pageNumber);


}
