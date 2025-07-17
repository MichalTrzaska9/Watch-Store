package com.example.service.serviceImplementation;

import com.example.entity.Cart;
import com.example.entity.Order;
import com.example.entity.RecipientDetails;
import com.example.entity.WatchOrder;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import com.example.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<WatchOrder> getOrdersByUser(Integer userId) {
        return orderRepository.findByUserDetailsIdOrderByOrderDateDesc(userId);
    }

    @Override
    public WatchOrder getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public Page<WatchOrder> getAllOrdersPagination(Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageSize, pageNumber, Sort.by(Sort.Order.desc("orderDate")));
        return orderRepository.findAll(pageable);
    }

    @Override
    public void saveOrder(Integer userId, Order order) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {

            WatchOrder watchOrder = new WatchOrder();

            watchOrder.setOrderId(UUID.randomUUID().toString());
            watchOrder.setOrderDate(LocalDateTime.now());

            watchOrder.setWatch(cart.getWatch());
            watchOrder.setPrice(cart.getWatch().getPrice());

            watchOrder.setQuantity(cart.getQuantity());
            watchOrder.setUserDetails(cart.getUser());

            watchOrder.setOrderStatus(OrderStatus.ORDER_IN_PROCESSING.getName());

            RecipientDetails recipientDetails = new RecipientDetails();
            recipientDetails.setName(order.getName());
            recipientDetails.setSurname(order.getSurname());
            recipientDetails.setEmail(order.getEmail());
            recipientDetails.setPhone(order.getPhone());
            recipientDetails.setStreet(order.getStreet());
            recipientDetails.setCity(order.getCity());
            recipientDetails.setCountry(order.getCountry());
            recipientDetails.setPostcode(order.getPostcode());

            watchOrder.setRecipientDetails(recipientDetails);

            orderRepository.save(watchOrder);
        }
    }

    @Override
    public WatchOrder updateOrderStatus(Integer id, String orderStatus) {
        Optional<WatchOrder> findByOrderId = orderRepository.findById(id);
        if (findByOrderId.isPresent()) {
            WatchOrder watchOrder = findByOrderId.get();
            watchOrder.setOrderStatus(orderStatus);
            return orderRepository.save(watchOrder);
        }
        return null;
    }


}
