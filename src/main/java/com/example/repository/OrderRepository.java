package com.example.repository;

import com.example.entity.WatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<WatchOrder, Integer> {

    WatchOrder findByOrderId(String orderId);

    List<WatchOrder> findByUserDetailsIdOrderByOrderDateDesc(Integer userId);

}
